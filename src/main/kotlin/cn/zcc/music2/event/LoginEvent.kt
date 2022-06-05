package cn.zcc.music2.event

import cn.zcc.music2.enum.CodeMsg
import cn.zcc.music2.info.RequestData
import cn.zcc.music2.service.sql.SqliteManager
import cn.zcc.music2.service.sql.TokenManager
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/event/login")
class LoginEvent {
    //token自动验效包
    @PostMapping("auto")
    @ResponseBody
    fun onAuto(
        @RequestParam("token") token: String
    ) : Any {
        val requestData = RequestData()
        val tokenToInfo = TokenManager.tokenToInfo(token)
        if (tokenToInfo != null){
            requestData.setCode(CodeMsg.C_0)
            requestData.data = TokenManager.tokenToInfo(token)!!.name
        }else{
            requestData.setCode(CodeMsg.C_403)
        }
        return requestData
    }

    //登录请求
    @PostMapping("login")
    fun onLogin(
        @RequestParam("username") username: String,
        @RequestParam("password") password: String
    ): Any {
        val requestData = RequestData()
        //验效前端提交过来的数据
        if (username == "" || password == "") {
            requestData.setCode(CodeMsg.C__0)
            return requestData
        }
        if (SqliteManager.login(username, password)) {
            requestData.setCode(CodeMsg.C_0)
            requestData.data = TokenManager.newToken(username, password)
        } else {
            requestData.setCode(CodeMsg.C_403)
        }
        return requestData
    }

    //注册请求
    @PostMapping("register")
    @ResponseBody
    fun onRegister(
        @RequestParam("username") username: String,
        @RequestParam("password") password: String,
        @RequestParam("confirm_password") confirm_password: String
    ): Any {
        val requestData = RequestData()
        //验效前端提交过来的数据
        if (username == "" || password == "" || confirm_password == "") {
            requestData.setCode(CodeMsg.C__0)
            return requestData
        }
        if (password != confirm_password) {
            requestData.setCode(CodeMsg.C_403)
            return requestData
        }

        //注册成功
        when (SqliteManager.register(username, password)) {
            0 -> {
                requestData.msg = "欢迎舰长上床(船)!~ 💕"
                requestData.code = CodeMsg.C_0.code
            }
            -1 -> {
                requestData.setCode(CodeMsg.C_40001)
            }
            else -> {
                requestData.msg = "用户可能存在了...换个名字再试试吧~"
                requestData.code = CodeMsg.C_403.code
            }
        }
        return requestData
    }
}