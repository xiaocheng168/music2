package cn.zcc.music2.event

import cn.zcc.music2.enum.CodeMsg
import cn.zcc.music2.info.RequestData
import cn.zcc.music2.service.sql.PlayManager
import cn.zcc.music2.service.sql.TokenManager
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/event/player")
@CrossOrigin(origins = ["*"], maxAge = 3600)
class Player {
    @PostMapping("updatePlayList")
    @ResponseBody
    fun onUpdatePlayList(
        @RequestParam("token") token: String,
        @RequestParam("type") type: Int,
        @RequestParam("id") mid: Int
    ): Any {
        val requestData = RequestData()
        //Token 未过期
        val userInfo = TokenManager.tokenToInfo(token)
        if (userInfo != null) {
            PlayManager.updatePlay(userInfo.id, type, mid)
        }
        requestData.setCode(CodeMsg.C_0)
        return requestData
    }

    @GetMapping("getPlayList")
    @ResponseBody
    fun onGetPlayList(@RequestParam("token") token: String) :Any{
        val requestData = RequestData()
        val tokenToInfo = TokenManager.tokenToInfo(token)
        //Token 验效
        if (tokenToInfo != null){
            try {
                requestData.data = PlayManager.getPlayList(tokenToInfo.id)
                requestData.setCode(CodeMsg.C_0)
            }catch (e: Exception){
                requestData.setCode(CodeMsg.C_40001)
                e.printStackTrace()
            }
        }
        return requestData
    }
}