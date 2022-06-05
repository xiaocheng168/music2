package cn.zcc.music2.tools

import java.net.URLEncoder
import java.security.MessageDigest

//编码处理类
abstract class CodeTools {
    companion object {
        //String 转 unicode
        @JvmStatic
        fun stringToUnicode(string: String): String {
            val sb: StringBuilder = StringBuilder()
            //遍历循环每个字符
            for (c in string) {
                //格式 \u + 字符hexCode!
                sb.append("\\u${Integer.toHexString(c.code)}")
            }
            return sb.toString()
        }

        @JvmStatic
        fun stringToUrlCode(string: String): String {
            return URLEncoder.encode(string, "UTF-8")
        }

        //字符串转 SHA256 加密字符串
        @JvmStatic
        fun stringToSha256(s: String): String {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            //遍历循环字符
            for (c in s) {
                //逐个加入到 加密算法文本中
                messageDigest.update(c.code.toByte())
            }
            val stringBuilder = StringBuilder()
            //处理加密
            for (byte in messageDigest.digest()) {
                val string = Integer.toHexString(byte.toInt() and 0xFF)
                //如果位数不够，补零
                if (string.length == 1) {
                    stringBuilder.append("0")
                }
                stringBuilder.append(string)
            }
            return stringBuilder.toString()
        }
    }
}