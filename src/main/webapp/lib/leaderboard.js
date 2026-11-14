async function getLeaderboard() {
    $.ajax({
        type: 'GET',
        url: 'api/leaderboard',
        cookie: cookiesConcat(),
        statusCode: {
            200: function () {
            },
            401: function () {
                alert("User, you are Unauthorized. You will be redirected to login");
                window.location.href = "login.html"
            },
            500: function (xhr) {

                alert("Oops! " + xhr.responseText)

            }
        },
        success: async function (data) {

            let receivedData = data["users"];
            console.log("array " + receivedData);

            console.log(receivedData[0].firstName);

            for (let counter = 1; counter <= receivedData.length; counter++) {
                if (receivedData[counter - 1].anonymous === true) {
                    $("#person" + counter + "nameLabel").html("Anonymous " + counter);
                    $("#scoreLabelUser" + counter).html(changeOfNumberFormatting(receivedData[counter - 1].points));

                } else if (receivedData[counter - 1].anonymous === false) {
                    $("#person" + counter + "nameLabel").html(receivedData[counter - 1].firstName + "  " + receivedData[counter - 1].lastName);
                    $("#scoreLabelUser" + counter).html(changeOfNumberFormatting(receivedData[counter - 1].points));

                }
            }
            for (let counterB = receivedData.length; counterB <= 10; counterB++) {
                $("#person" + counterB + "nameLabel").html("ㅤ");
                $("#scoreLabelUser" + counterB).html("ㅤ");


            }

            console.log("fetched data: " + await fetchUserData())
            console.log("fetched data Name: " + (await fetchUserData())[0])
            $("#currentUserNameLabel").html((await fetchUserData())[0]);
            $("#currentUserScoreLabel").html(changeOfNumberFormatting(await fetchUserData())[5]);


        },
        contentType: 'application/json',
        dataType: 'json'
    })
}

async function manageLeaderboard(data){
    // let leaderboardData = getLeaderboard();
   // let leaderboardDataJson = data;
    //let leaderboardDataJson = getLeaderboard();
    //let leaderboardDataObject = JSON.parse(leaderboardDataJson);

    //let leaderboardDataObject = JSON.parse(data);

    //console.log(data);
    //console.log("Chosen user:"+data[0])
    //let theName = data.name;
    //console.log(theName);

    // let firstName = leaderboardDataObject.firstName;
    // let points = leaderboardDataObject.points;
    // let isAnonymous = leaderboardDataObject.isAnonymous;


    // if (isAnonymous === true){
    //     firstName = "Anonymous";
    //     lastName = "Anonymous";
    // }
    //return firstName, points;

    //let $('#nameLabel').val() = firstName;


    // for (element in leaderboardData){
    //     if (element.key === "firstname"){
    //         element.getFirstName();
    //     }
    // }
}

function changeOfNumberFormatting(originalNumber) {
        if (originalNumber < 1e3) {
            return originalNumber;
        }
        if (originalNumber >= 1e3 && originalNumber < 1e6) {
            return +(originalNumber / 1e3).toFixed(1) + "k";
        }
        if (originalNumber >= 1e6 && originalNumber < 1e9) {
            return +(originalNumber / 1e6).toFixed(1) + "m";
        }
        if (originalNumber >= 1e9 && originalNumber < 1e12) {
            return +(originalNumber / 1e9).toFixed(1) + "b";
        }
        if (originalNumber >= 1e12) {
            return +(originalNumber / 1e12).toFixed(1) + "t";
        }
}