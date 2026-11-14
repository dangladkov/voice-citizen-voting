async function uploadImage(fileInput, resource, elementID) {
    // resource like "idea/benefit/project/news"
    const fd = new FormData();
    for (let i = 0; i < fileInput.length; i++) {
        fd.append("image", fileInput[i]);
    }
    await fetch("api/filerepo/upload-image/"+resource+"/"+elementID, {
        "headers": {
            "accept": "*/*",
        },
        "body": fd,
        "method": "POST",
        "cookie": cookiesConcat()
    })
}

async function downloadImage(imgElement, resource, elementID, filename) {
    let url = "api/filerepo/fetch-image/"+resource+"/"+elementID+"/"+filename
    return new Promise((resolve, reject) => {
        fetch(url)
            .then(response => response.blob())
            .then(blob => {
                imgElement.src = URL.createObjectURL(blob)
                resolve(imgElement)
            })
            .catch(error => {
                console.error('Error retrieving the image:', error);
                reject(error)
            })
    })
}