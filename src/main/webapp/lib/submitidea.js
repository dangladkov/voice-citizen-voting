async function submitIdeaDetails (){
    let title = $('#titleLabel').val()
    let description = $('#descriptionLabel').val()
    let image = 'image'
    let location = $('#locationInput').val()
    let date = $('#dateLabel').val()
    // if ($('#category1').checked()) {
    //     category = 1
    // } else if ($('#category2').checked()) {
    //     category = 2
    // }

    let userID = Cookies.get('userID')

    //let counter = 0;
    let chosenImage = $('#imageUpload').prop('files');
    let imageToUpload = new FormData();
    imageToUpload.append('file',chosenImage);

    $.ajax({
        type: 'POST',
        url: 'api/ideas/submit-idea',
        data: JSON.stringify({title: title, description: description, location: location, date: date, proposerUserID: userID}),
        cookie: cookiesConcat(),
        statusCode: {
            200: function () {
                console.log("Idea submitted!")
                window.location.href = "index.html"
            },
            401: function () {
                alert("User, you are Unauthorized. You will be redirected to login");
                window.location.href = "login.html"
            },
            403: function () {
                alert("User, this action is forbidden. You have insufficient access level")
                //window.location.href = "index.html"
            },
            500: function (xhr) {
                alert("Oops! " + xhr.responseText)
                //window.location.href = "index.html"
            }
        },
        success: async function (data) {
            await uploadImage($("#imageUpload").prop('files'), "idea", data)
            
        },
        contentType: "application/json",
        dataType: 'json'
    })

}

async function submitImageIdea(ideaID, fileinput) {
    const fd = new FormData();
    for (let i = 0; i < fileinput.length; i++) {
        fd.append("image", fileinput[i]); // JS also lets you append the whole fileList, but tomcat does not like that.
    }

    await fetch("api/filerepo/idea/upload-image/" + ideaID, {
        "headers": {  // Tomcat breaks when a Content-type is given, so we skip it.
            "accept": "*/*",
        },
        "body": fd,
        "method": "POST",
        "cookie": cookiesConcat()
    });
}