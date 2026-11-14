async function fetchUserData() {
    let userType = null
    return new Promise((resolve, reject) => {
        $.ajax({
            type: 'GET',
            url: 'api/user/fetch',
            cookie: cookiesConcat(),
            statusCode: {
                200: function () {
                    // alert("User data fetched")
                },
                500: function () {
                    alert("Something went wrong");
                    window.location.href = "index.html";
                    reject("Something went wrong"); // Reject the promise on error
                }
            },
            success: function (data) {
                let userFirstName = data["name"];
                let userSurname = data["surname"];
                let userPhoneNumber = data["phoneNumber"];
                let userEmail = data["email"];
                let userAccessLevel = data["accessLevel"];
                let userPoints = data["points"]
                let userIsAnonymous = data["isAnonymous"]

                if (userAccessLevel === 0) {
                    userType = "citizen";
                } else if (userAccessLevel === 1) {
                    userType = "admin";
                }

                ;
                resolve([userFirstName, userSurname, userPhoneNumber, userEmail, userAccessLevel, userPoints, userIsAnonymous]); // Resolve the promise with user data
            },
            contentType: "application/json",
            dataType: 'json'
        });
    });
}

async function currentUserName(){
    $.ajax({
        type: 'GET',
        url: 'api/user/fetch',
        cookie: cookiesConcat(),
        statusCode: {
            200: function () {
            },
            500: function () {
                alert("Something went wrong (username)")
                //window.location.href = "login.html"
            }
        },
        success: function (data) {
            console.log(data)
            let userFirstName = data["name"]
            let userLastName = data["surname"]
            let points = data["points"]
            $("#username").html(userFirstName + " " + userLastName)
            $("#usernameCompact").html(userFirstName + " " + userLastName)
            $("#points").html(changeOfNumberFormatting(points))
            //$("#points").html(points)
        },
        contentType: "application/json",
        dataType: 'json'
    })
}

async function currentUserNameUserpage(){
    $.ajax({
        type: 'GET',
        url: 'api/user/fetch',
        cookie: cookiesConcat(),
        statusCode: {
            200: function () {
            },
            500: function () {
                alert("Something went wrong (username)")
                //window.location.href = "login.html"
            }
        },
        success: function (data) {
            console.log(data)
            let userFirstName = data["name"]
            let userLastName = data["surname"]
            let points = data["points"]
            $("#userFirstName").html(userFirstName)
            $("#userLastName").html(userLastName)
            console.log("These are the points: "+points)
            $("#points").html(changeOfNumberFormatting(points))
            
        },
        contentType: "application/json",
        dataType: 'json'
    })
}

async function updateAccessLevel(userID, accessLevel) {
    //let promotionLevel = $('#PromButton').val() // chosen button should provide value of the promoted/demoted access level
    //let userID = Cookies.get('userID')

    $.ajax({
        type: 'POST',
        url: 'api/user/'+userID+'set-access-level'+accessLevel,
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
                //window.location.href = "index.html"
            }
        },
        success: function () {
            
        },
        contentType: 'application/json',
        dataType: 'json'
    })
}

async function updateAccountData(dataToChange) {
    let userData = await fetchUserData();
    //let isAnonymous = false
    if (dataToChange === 0) {
        if ($('#userPhoneNum').val().length > 0) {
            userData[2] = $('#userPhoneNum').val()
        }
    } else if (dataToChange === 1) {
        if ($('#userEmail').val().length > 0) {
            userData[3] = $('#userEmail').val()
        }
    }
    $.ajax({
        type: 'POST',
        url: 'api/user/update',
        data: JSON.stringify({name: userData[0], surname: userData[1], phoneNumber: userData[2], email: userData[3]}),
        cookie: cookiesConcat(),
        statusCode: {
            200: function () {
                window.location.href = "index.html"
            },
            401: function () {
                alert("User, you are Unauthorized. You will be redirected to login");
                window.location.href = "login.html"
            },
            500: function (xhr) {
                alert("Oops! " + xhr.responseText)
                //window.location.href = "index.html"
            }
        },
        success: function () {
            
        },
        contentType: 'application/json',
        dataType: 'json'
    })
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