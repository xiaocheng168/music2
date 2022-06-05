package cn.zcc.music2.service.api

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

//网络请求处理类
class HttpService {
    //发送请求
    fun post(apiUrl: String, hashMap: HashMap<String, String>): String {
        return request(apiUrl, hashMap, "POST")
    }

    //发送请求
    fun get(apiUrl: String, hashMap: HashMap<String, String>): String {
        return request(apiUrl, hashMap, "GET")
    }

    //发送请求
    private fun request(apiUrl: String, hashMap: HashMap<String, String>, type: String): String {
        var requestData = ""
        for (mutableEntry in hashMap) {
            requestData += mutableEntry.key + "=" + mutableEntry.value + "&"
        }
        //开始访问请求
        val url =
            URL("${apiUrl}?${requestData}")
        val openConnection = url.openConnection() as HttpURLConnection
        openConnection.requestMethod = type
        val bufferedReader = BufferedReader(InputStreamReader(openConnection.inputStream))
        val backData: String = bufferedReader.readText()
        //关闭输入流
        bufferedReader.close()
        return backData
    }
}