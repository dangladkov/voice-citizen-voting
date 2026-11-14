async function loginUser() {
    let userIdentifier = $('#usernameInput').val();
    let password = $('#passwordInput').val();
    let hashedPassword = await messageDigester(password);
    if (userIdentifier.length > 0 && password.length > 0) {
        $.ajax({
            type: 'POST',
            url: 'api/user/login',
            data: JSON.stringify({userIdentifier: userIdentifier, pwd: password}),
            statusCode: {
                200: function () {
                },
                500: function (xhr) {
                    alert("Oops! "+xhr.responseText)

                }
            },
            success: function (data) {
                let userID = data['userID'];
                let token = data['token'];

                console.log("token: " + token + " userID: " + userID);
                addDataSessionCookie(token,userID,0.0833333333);
                window.location.href = 'index.html';
            },
            contentType: "application/json",
            dataType: 'json'
        }).fail(function(data, textStatus, xhr) {
            errorFilter(xhr, data);
            console.log(xhr, data);
        });
    } else {
        alert("Phone number and password must be input");
    }
}