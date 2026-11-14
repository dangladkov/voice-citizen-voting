async function addDataSessionCookie(token, userID, chosenDaysExpire){
    let path = "http://localhost:8080/voice/";
    const theDate = new Date();
    theDate.setTime(theDate.getTime()+chosenDaysExpire*24*60*60*1000);
    let expireDate = "expires="+ theDate.toUTCString();
    //let cookieData =  document.cookie = ("Authentication" +"="+"token: " + token + " userID: " + userID + ";" + expireDate + ";" + "path=" + path + ";" + "Secure|");
    //let cookieData =  document.cookie = ("Authentication " + "Cookie=" + "token= " + token + " path= " + path + " userID= " + userID);
    Cookies.set("token", token, {path: path}, {expires: expireDate})
    Cookies.set("userID", userID, {path: path}, {expires: expireDate})
}

function cookiesConcat() {
    Cookies.set("Authentication", "token:/" + Cookies.get("token") + "/userID:/" + Cookies.get("userID"))
    return "Authentication=token:/" + Cookies.get("token") + "/" + "userID:/" + Cookies.get("userID")
}