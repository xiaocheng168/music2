package cn.zcc.music2.service.sql

import cn.zcc.music2.service.api.CloudMusic
import com.alibaba.fastjson.JSONArray

abstract class PlayManager {
    companion object {
        //更新用户最新播放的音乐
        @JvmStatic
        fun updatePlay(uid: Int, type: Int, sid: Int) {
            val query = SqliteManager.query("select id from play_list where id = $sid and play_user_id = $uid")
            //如果存在就修改播放时间，以便后期排序使用
            while (query.next()) {
                //更新新的播放时间
                SqliteManager.execute(
                    "update play_list set play_time_long = ${System.currentTimeMillis()} where id = $sid and play_user_id = $uid"
                )
                query.close()
                return
            }
            //不存在，插入新的行
            SqliteManager.execute("insert into play_list values($type,$sid,${System.currentTimeMillis()},$uid)")
        }

        //获取播放列表
        @JvmStatic
        fun getPlayList(uid: Int) : JSONArray{
            val query =
                SqliteManager.query("select * from play_list where play_user_id = $uid order by play_time_long")
            val jsonArray = JSONArray()
            //遍历循环找到的播放列表 Row
            while (query.next()) {
                val musicIdToInfo = CloudMusic.musicIdToInfo(query.getLong("id"))
                //将音乐信息类打包成 json
                jsonArray.add(musicIdToInfo.packJson())
            }
            return jsonArray
        }
    }
}