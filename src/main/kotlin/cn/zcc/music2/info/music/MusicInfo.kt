package cn.zcc.music2.info.music

import cn.zcc.music2.service.api.CloudMusic
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject

class MusicInfo(name: String, id: Long, type: Int, duration: Long) {
    val name: String
    val id: Long
    var authors: HashMap<Long, AuthorInfo> = LinkedHashMap()
    val playUrl: String
    val duration: Long
    val type: Int

    init {
        this.name = name
        this.id = id
        //判断播放平台
        when(type){
            1->{
                this.playUrl = "https://music.163.com/song/media/outer/url?id=${this.id}.mp3"
            }
            else ->{
                this.playUrl = "not url"
            }
        }

        this.type = type
        this.duration = duration
    }

    fun setAuthors(jsonObject: JSONObject){
        //处理创作者
        for (any in jsonObject.getJSONArray("artists")) {
            val authorData = any as JSONObject
            val authorInfo = AuthorInfo(authorData.getLong("id"), authorData.getString("name"), authorData)
            authors[authorInfo.id] = authorInfo
        }
    }

    private fun getLyric(): LyricsInfo {
        return CloudMusic.getLyrics(this.id)
    }

    fun packJson(): JSONObject {
        val musicJson = JSONObject()
        musicJson["name"] = this.name
        musicJson["id"] = this.id
        val authorJson = JSONArray()
        for (author in this.authors) {
            val authorInfoJson = JSONObject()
            val v = author.value
            authorInfoJson["name"] = v.name
            authorInfoJson["id"] = v.id
            authorJson.add(authorInfoJson)
        }
        musicJson["author"] = authorJson
        musicJson["url"] = this.playUrl
        musicJson["playTime"] = this.duration
        musicJson["lyrics"] = this.getLyric()
        musicJson["type"] = 1
        return musicJson
    }
}