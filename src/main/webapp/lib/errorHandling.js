async function errorFilter(statusCode, data){
    switch (statusCode){
        case (200):
            break;
        case (401):
            alert("User, you are Unauthorized. You will be redirected to login");
            window.location.href = "login.html";
            break;
        case (403):
            alert("User, this action is forbidden. You have insufficient access level")
            break;
        case (404):
            break;
        case (413):
            alert(data['response'])
            break;
        case (405):
            alert(data['response'])
            break;
        case (500):
            alert(data['response'])
            alert("Oops! " + xhr.responseText)
            break;

    }
}