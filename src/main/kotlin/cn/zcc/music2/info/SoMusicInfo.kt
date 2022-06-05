package cn.zcc.music2.info

import cn.zcc.music2.info.music.MusicInfo
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject

//搜索返回JSON
//返回对象平台类型 1 = 网易云音乐
class SoMusicInfo(jsonData: String, type: Int) {
    val jsonData: String
    val type: Int
    val soMusicList: HashMap<Long, MusicInfo> = LinkedHashMap()

    init {
        this.jsonData = jsonData
        this.type = type
        when (type) {
            //网易云
            1 -> {
                val jsonObject = JSON.parseObject(jsonData)
                val songs = jsonObject.getJSONObject("result").getJSONArray("songs")
                //遍历处理音乐信息类
                for (s in songs) {
                    //JSON原文本数据
                    val song = s as JSONObject
                    //打包成音乐信息类
                    val musicInfo = MusicInfo(song.getString("name"), song.getLong("id"),1,song.getLong("duration"))
                    musicInfo.setAuthors(song)
                    //加入搜索HasMap做统计
                    soMusicList[musicInfo.id] = musicInfo
                }
            }
        }
    }
}