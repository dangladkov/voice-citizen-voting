

//function call when buttton is pressed
//comapre both password in PLAINTEXT
// Hash one of the passwords )one input field)
//generate JQuery "/api/auth/signup"

async function passwordComparison (userPassword, userPasswordConfirmation){
    if (userPassword === userPasswordConfirmation){
        return await messageDigester(userPassword);
    }
    else{
        console.log("Passwords are not equivalent")
        return "Passwords are not equivalent";
    }
}

async function signupUser () { //token, hashed password
    let userPassword = $("#signupPassword").val();
    let userPasswordConfirmation = $("#signupConfirmPassword").val();
    let firstName = $("#signupFirstName").val();
    let lastName = $("#signupLastName").val();
    let phoneNumber = $("#signupPhoneNumber").val();
    let email = $("#signupEmail").val();

    const hashedPasswordToSend = await passwordComparison(userPassword, userPasswordConfirmation);

    $.ajax({
        type: 'POST',
        url: 'api/user/sign-up',
        data: JSON.stringify({name: firstName, surname: lastName, phoneNumber: phoneNumber, email: email, pwd: userPassword}), //hashedPasswordToSend:hashedPasswordToSend
        statusCode: {
            200: function () {
            },
            500: function (xhr) {
                alert("Oops! " + xhr.responseText)
            }
        },
        success: function (data) {
            let token = data["token"];
            let userID = data["userID"];
            addDataSessionCookie(token, userID, 0.08333333333);
            window.location.href = "index.html";
        },
        contentType: "application/json",
        dataType: 'json'
    })
}


