// webauthn.js

function bufferToBase64URL(buffer) {
    const bytes = new Uint8Array(buffer);
    let str = "";
    for (const b of bytes) str += String.fromCharCode(b);
    return btoa(str)
        .replace(/\+/g, "-")
        .replace(/\//g, "_")
        .replace(/=+$/, "");
}

function base64URLToBuffer(s) {
    s = s.replace(/-/g, "+").replace(/_/g, "/");
    const pad = s.length % 4;
    if (pad) s += "=".repeat(4 - pad);
    const bin = atob(s);
    const buf = new ArrayBuffer(bin.length);
    const arr = new Uint8Array(buf);
    for (let i = 0; i < bin.length; i++) arr[i] = bin.charCodeAt(i);
    return buf;
}

async function register() {
    const username = document.getElementById("username").value;

    // 1) Fetch registration options
    const resp = await fetch("/auth/yubikey/register/options", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({username}),
    });
    const opts = await resp.json();

    // 2) Decode challenge & user ID
    opts.challenge = base64URLToBuffer(opts.challenge);
    opts.user.id = base64URLToBuffer(opts.user.id);

    // 3) Decode any excluded credentials
    if (opts.excludeCredentials) {
        opts.excludeCredentials = opts.excludeCredentials.map((c) => ({
            ...c,
            id: base64URLToBuffer(c.id),
        }));
    }

    // 4) Create the credential
    const cred = await navigator.credentials.create({publicKey: opts});

    // 5) Build payload (including empty clientExtensionResults)
    const data = {
        id: cred.id,
        rawId: bufferToBase64URL(cred.rawId),
        type: cred.type,
        response: {
            attestationObject: bufferToBase64URL(
                cred.response.attestationObject
            ),
            clientDataJSON: bufferToBase64URL(cred.response.clientDataJSON),
        },
        clientExtensionResults: {},
    };

    // 6) Send to server
    const fin = await fetch("/auth/yubikey/register", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data),
    });
    //(await fin.text());

    const text= await fin.text();
    if (text.startsWith("OK")) {
        // redirect to the login page
        window.location.href = "/auth/yubikey/login";
    } else {
        // on failure, show the error
        alert("Registration failed: " + text);
    }
}

async function login() {
    const username = document.getElementById("username").value;

    // 1) Fetch authentication options
    const resp = await fetch("/auth/yubikey/login/options", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({username}),
    });
    const opts = await resp.json();

    // 2) Decode challenge
    opts.challenge = base64URLToBuffer(opts.challenge);

    // 3) Normalize and decode allowCredentials array
    if (opts.allowCredentials) {
        opts.allowCredentials = opts.allowCredentials.map((c) => ({
            id: base64URLToBuffer(c.id),
            type: c.type,
            transports: Array.isArray(c.transports)
                ? c.transports
                : c.transports
                    ? [c.transports]
                    : undefined,
        }));
    }

    // 4) Get the assertion
    const assertion = await navigator.credentials.get({publicKey: opts});

    // 5) Build payload
    const data = {
        id: assertion.id,
        rawId: bufferToBase64URL(assertion.rawId),
        type: assertion.type,
        response: {
            authenticatorData: bufferToBase64URL(
                assertion.response.authenticatorData
            ),
            clientDataJSON: bufferToBase64URL(assertion.response.clientDataJSON),
            signature: bufferToBase64URL(assertion.response.signature),
            userHandle: assertion.response.userHandle
                ? bufferToBase64URL(assertion.response.userHandle)
                : null,
        },
        clientExtensionResults: {},
    };

    // // 6) Send to server
    // const fin = await fetch("/auth/yubikey/login", {
    //     method: "POST",
    //     headers: {"Content-Type": "application/json"},
    //     credentials:"same-origin",
    //     body: JSON.stringify(data),
    // });
    // //alert(await fin.text());
    // //get the username from the form
    // const text = await fin.text();
    // if (text.startsWith("OK")) {
    //     window.location.href = "/auth/yubikey/welcome";
    // } else {
    //     alert("Login failed: " + text);
    // }
    try {
        const fin = await fetch("/auth/yubikey/login", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            credentials: "same-origin",
            body: JSON.stringify(data),
        });
        const response = await fin.json();
        if (response.status === "OK") {
            window.location.href = "/auth/yubikey/welcome";
        } else {
            alert("Login failed: " + response.error);
        }
    } catch (error) {
        alert("Network error: Unable to connect to server");
        console.error("Login error:", error);
    }
}
