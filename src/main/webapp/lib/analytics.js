
async function getAnalytics(){
    return new Promise((resolve, reject) => {
        $.ajax({
            type: 'GET',
            url: 'api/analytics',
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
                let totalProjects = data["totalProjects"]
                let totalPoints = data["totalPoints"]
                let totalNumberOfUsers = data["totalNumbersOfUsers"]
                let totalNumberOfVotes = data["totalNumberOfVotes"]

                resolve ([totalProjects, totalPoints, totalNumberOfUsers, totalNumberOfVotes]);
            },
            contentType: 'application/json',
            dataType: 'json'
        });
    })
}

function updateIndexStats(){
    $("#totalProjectsNum").html(( getAnalytics())[0]);
    $("#pointsSpentNum").html(( getAnalytics())[1]);
    $("#totalUsers").html(( getAnalytics())[2]);
    $("#castedVotesNum").html(( getAnalytics())[3]);
}
