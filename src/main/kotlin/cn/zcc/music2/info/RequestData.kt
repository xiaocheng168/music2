package cn.zcc.music2.info

import cn.zcc.music2.enum.CodeMsg
import com.alibaba.fastjson.JSONObject

class RequestData() {
    var msg: String = "啥也没有..."
    var code: Int = -114514
    var data: Any? = null

    override fun toString(): String {
        val jsonObject = JSONObject()
        jsonObject["msg"] = msg
        jsonObject["code"] = code
        jsonObject["data"] = data
        return jsonObject.toString()
    }
    fun setCode(codeMsg: CodeMsg){
        this.code = codeMsg.code
        this.msg = codeMsg.msg
    }


}