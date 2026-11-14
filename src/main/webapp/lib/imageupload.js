async function uploadFile (fileinput, resourcetype, resource){
    const fd = new FormData();

    for (let file of fileinput.files) {
        fd.append("file", file);
    }
    fetch("/api/upload-image/" + resourcetype + "/" + resource, {
        "headers": {
            "accept": "*/*",
        },
        "body": fd,
        "method": "POST",
    });
}