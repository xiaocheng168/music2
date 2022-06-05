package cn.zcc.music2.info.music

import com.alibaba.fastjson.JSONObject

class AuthorInfo(id: Long, name: String, jsonObject: JSONObject) {
    val id: Long
    val name: String
    val jsonObject: JSONObject

    init {
        this.id = id
        this.name = name
        this.jsonObject = jsonObject
    }
}