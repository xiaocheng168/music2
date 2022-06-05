package cn.zcc.music2.service.sql

import cn.zcc.music2.info.user.UserInfo
import cn.zcc.music2.tools.CodeTools
import java.util.*

abstract class TokenManager {
    companion object {
        @JvmStatic
        //打包新Token
        fun newToken(name: String, pwd: String): String {
            val toKenData = StringBuilder()
            // 时间戳,用户id,用户名,密码(sha256)
            toKenData.append(System.currentTimeMillis()).append(",")
            toKenData.append(SqliteManager.nameToUid(name)).append(",")
            toKenData.append(name).append(",")
            toKenData.append(CodeTools.stringToSha256(pwd))
            val uuid = UUID.randomUUID().toString()
            SqliteManager.execute("insert into temp_token values ('$uuid','${toKenData}')")
            return uuid
        }

        //Token 获取用户基本信息
        //返回前提是 此Token未过期，并且可登录 不存在返回 null
        fun tokenToInfo(token: String): UserInfo? {
            val query = SqliteManager.query("select data from temp_token where token = '$token'")
            while (query.next()) {
                val string = query.getString("data").split(",")
                val name: String = string[2]
                val sha256Pwd: String = string[3]
                if (!SqliteManager.loginNotSha256(name, sha256Pwd)) {
                    return null
                }
                return UserInfo(string[1].toInt(), string[2])
            }
            return null
        }
    }
}