
async function messageDigester(promiseMessage) {
    const salt1 = "世界";
    const salt2 = "こんにちは";

    const encoder = new TextEncoder();
    const encodedMessage = encoder.encode(salt1+promiseMessage+salt2);

    const hashBuffer = await crypto.subtle.digest("SHA-512", encodedMessage);

    const hashArray = Array.from(new Uint8Array(hashBuffer));
    const hashHex = hashArray.map((b) => b.toString(16).padStart(2, "0")).join("");


    return hashHex;

}
