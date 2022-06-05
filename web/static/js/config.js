const mainApi = "http://192.168.1.104:5555/"
const api = {
    auto: mainApi + "event/login/auto",
    loginApi: mainApi + "event/login/login",
    registerApi: mainApi + "event/login/register",
    soMusic: mainApi + "event/search/soMusic",
    updatePlayList: mainApi + "event/player/updatePlayList",
    getPlayList: mainApi + "event/player/getPlayList"
}

//消息处理
function message(msg, typed, delay) {
    app.$message({
        type: typed,
        message: msg,
        duration: delay
    })
}

//自动消息处理
function autoMessage(rd) {
    if (rd.code === -40001) {
        message(rd.msg, "error", 10000)
    }
    if (rd.code === -1) {
        message(rd.msg, "warning", 3000)
    }
}

//错误消息处理
function errorMessage(e) {
    message("无法与服务器建立连接", "error", 10000)
}