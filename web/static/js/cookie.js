//Cookie Setting

//SetCookie
function setCookie(key, value, timeH) {
    var date = new Date();
    var ms = timeH * 3600 * 1000;
    date.setTime(date.getTime() + ms);
    var cookie = key + "=" + value + ";path=/; expires=" + date.toGMTString();
    document.cookie = cookie;
}

//GetCookie
function getCookie(key) {
    var value = "";
    var cookie = document.cookie.split("; ");
    cookie.map(function (n) {
        var c = n.split("=");
        if (c[0] == key) {
            value = c[1];
        }
    });
    return value;
}