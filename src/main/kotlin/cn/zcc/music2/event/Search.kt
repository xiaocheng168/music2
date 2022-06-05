package cn.zcc.music2.event

import cn.zcc.music2.enum.CodeMsg
import cn.zcc.music2.info.RequestData
import cn.zcc.music2.service.api.CloudMusic
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/event/search")
@CrossOrigin(origins = ["*"], maxAge = 3600)
class Search {
    @PostMapping("soMusic")
    fun onSoMusic(
        @RequestParam("so") soText: String,
        @RequestParam("type") type: Int
    ): Any {
        val requestData = RequestData()
        if (soText == "") {
            requestData.setCode(CodeMsg.C__0)
            return requestData
        }
        try {
            when (type) {
                1 -> {
                    //网易云音乐
                    val soMusic = CloudMusic.soMusic(soText, type, 12)
                    val jsonArray = JSONArray()
                    for (mutableEntry in soMusic.soMusicList) {
                        val musicJson = mutableEntry.value.packJson()
                        jsonArray.add(musicJson)
                    }
                    requestData.setCode(CodeMsg.C_0)
                    requestData.data = jsonArray
                }
                2 -> {
                    requestData.code -CodeMsg.C__0.code
                    requestData.msg = "未开放"
                }
                3 -> {
                    requestData.code -CodeMsg.C__0.code
                    requestData.msg = "未开放"
                }
                else -> {
                    requestData.code -CodeMsg.C__0.code
                    requestData.msg = "未知平台"
                }
            }
        } catch (e: Exception) {
            requestData.setCode(CodeMsg.C_40001)
            e.printStackTrace()
        }

        return requestData
    }
}