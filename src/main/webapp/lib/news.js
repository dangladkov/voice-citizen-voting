
async function getNewsDetails(newsID){
    return new Promise((resolve, reject) => {
        $.ajax({
            type: 'GET',
            url: 'api/news/'+newsID,
            cookie: cookiesConcat(),
            statusCode: {
                200: function () {
                },
                401: function () {
                    alert("User is unauthorized");
                    window.location.href = "login.html";
                    reject("User is unauthorized");
                },
                500: function () {
                    alert("Something went wrong");
                    //window.location.href = "index.html";
                    reject("Something went wrong");
                }
            },
            success: async function (data) {
                let newsID = data["newsID"]
                let newsTitle = data["title"];
                let newsDescription = data["description"];

                resolve ([newsID, newsTitle, newsDescription]);
            },
            contentType: 'application/json',
            dataType: 'json'
        });
    })
}

async function getAllNews(){
        return new Promise((resolve, reject) => {
            $.ajax({
                type: 'GET',
                url: 'api/news/fetch-all',
                cookie: cookiesConcat(),
                statusCode: {
                    200: function () {
                    },
                    401: function () {
                        alert("User is unauthorized")
                        window.location.href = "login.html"
                        reject("User is unauthorized");
                    },
                    500: function () {
                        alert("Something went wrong")
                        //window.location.href = "index.html"
                        reject("Something went wrong");
                    }
                },
                success: async function (data) {
                    let myData = data["newsIDs"];
                    console.log("Data: " + myData);
                    console.log("Data split: " + myData);

                    resolve([myData]);

                },
                contentType: 'application/json',
                dataType: 'json'
            });
        });
}



async function loopNews() {
    let data = await getAllNews()
    console.log("data"+data[0].length)
    let allNews = ``
    for (let i = 0; i < data[0].length; i++) {
        let news = await getNewsDetails(data[0][i])
        console.log("News Details: "+news)
        allNews += `<div class="newsPanel shadow rounded-2">
            <div class="newsGrid">
                <div class="newsArticle">
                    <div class="newsTitle fw-bold">${news[1]}</div>

                    <div class="newsContent">
                        ${news[2]}
                    </div>
                </div>

                <div class="newsImageContainer">
                    <img src="images/park-construction.jpg" class="newsImage">
                </div>
            </div>
        </div>`
    }
    $('#newsWrapper').html(allNews)
    console.log("loop ended")
}

async function publishNews(){
    let newsTitle = $('#titleLabel').val();
    let newsDescription = $('#descriptionLabel').val();
    console.log("Title: "+newsTitle+" and the description: "+newsDescription);

    $.ajax({
        type: 'POST',
        url: 'api/news/submit',
        data: JSON.stringify({title: newsTitle, description: newsDescription}),
        cookie: cookiesConcat(),
        statusCode: {
            200: function () {
                //alert("news submitted")
                window.location.href = "news.html"
                //await uploadImage($("#resourceImagesInput").prop('files'), "news", )
            },
            401: function () {
                alert("User, you are Unauthorized. You will be redirected to login");
                window.location.href = "login.html"
            },
            403: function () {
                alert("User, this action is forbidden. You have insufficient access level")
                //window.location.href = "login.html"
            },
            500: function (xhr) {
                alert("Oops! " + xhr.responseText)

            }
        },
        success: function () {
        },
        contentType: "application/json",
        dataType: 'json'
    })
}

async function deleteNews(newsID){
    $.ajax({
        type: 'DELETE',
        url: 'api/news/'+newsID,
        cookie: cookiesConcat(),
        statusCode: {
            200: function () {
                alert("News Deleted")
                window.location.href = "newsmanagement.html"
            },
            401: function () {
                alert("User, you are Unauthorized. You will be redirected to login");
                window.location.href = "login.html"
            },
            500: function (xhr) {
                alert("Oops! " + xhr.responseText)
                //window.location.href = "login.html"
            }
        },
        success: function () {
        },
        contentType: 'application/json',
        dataType: 'json'
    })
}


