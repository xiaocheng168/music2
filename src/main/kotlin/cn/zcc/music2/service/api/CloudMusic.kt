package cn.zcc.music2.service.api

import cn.zcc.music2.info.music.LyricsInfo
import cn.zcc.music2.info.SoMusicInfo
import cn.zcc.music2.info.music.MusicInfo
import cn.zcc.music2.tools.CodeTools
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject

//网易云音乐接口
//API document
//https://note.51simple.com/netease-music-api-interface.html
abstract class CloudMusic {
    companion object {
        //API LIST
        const val soMusicAPI = "https://music.163.com/api/search/get"
        const val lyricsAPI = "https://music.163.com/api/song/media"
        const val musicInfoAPI = "https://music.163.com/api/song/detail?ids="

        //搜索音乐
        @JvmStatic
        fun soMusic(name: String, type: Int, soSize: Int): SoMusicInfo {
            val requestData: HashMap<String, String> = LinkedHashMap()
            requestData["s"] = CodeTools.stringToUrlCode(name)
            requestData["type"] = type.toString()
            requestData["offset"] = "0"
            requestData["limit"] = soSize.toString()
            val backData = HttpService().get(soMusicAPI, requestData)
            return SoMusicInfo(backData, 1)
        }

        @JvmStatic
        fun getLyrics(id: Long): LyricsInfo {
            val requestData: HashMap<String, String> = LinkedHashMap()
            requestData["id"] = id.toString()
            val backData = HttpService().get(lyricsAPI, requestData)
            return LyricsInfo(id, backData)
        }
        //Id转音乐信息类
        @JvmStatic
        fun musicIdToInfo(id: Long) : MusicInfo{
            val httpService = HttpService()
            val getMapping = httpService.get("$musicInfoAPI[$id]",HashMap())
            val jsonObject = JSONObject.parseObject(getMapping).getJSONArray("songs")[0] as JSONObject
            val musicInfo = MusicInfo(jsonObject.getString("name"),id,1,jsonObject.getLong("duration"))
            musicInfo.setAuthors(jsonObject)
            return musicInfo
        }
    }
}