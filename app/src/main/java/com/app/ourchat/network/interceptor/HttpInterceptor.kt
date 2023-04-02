package com.app.ourchat.network.interceptor

import com.app.ourchat.utils.Constant
import com.app.ourchat.utils.SHA1Util
import okhttp3.Interceptor
import okhttp3.Response
import java.util.Random
import kotlin.collections.HashMap

class HttpInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val newBuilder = request.newBuilder()

        val headerParams = createHeaderParams()
        newBuilder.addHeader("App-Key",Constant.rongCloudAppKey)
        newBuilder.addHeader("Nonce",headerParams["nonce"]?:"")
        newBuilder.addHeader("Timestamp",headerParams["timestamp"]?:"")
        newBuilder.addHeader("Signature",headerParams["signature"]?:"")

        val newRequest = newBuilder.build()
        return chain.proceed(newRequest)
    }

    private fun getNonce() = Random().nextInt(1000000)

    private fun getSha1(appSecret:String,nonce:String,timestamp:String): String {
        return SHA1Util.shaEncode(appSecret+nonce+timestamp)
    }


    private fun createHeaderParams() : HashMap<String,String> {
        val secret = Constant.rongCloudAppSecret
        val nonce = getNonce().toString()
        val timestamp = System.currentTimeMillis().toString()
        //将以下三个字符串按顺序（App Secret + Nonce + Timestamp）拼接成一个字符串，进行 SHA1 哈希计算。
        val signature = getSha1(secret,nonce,timestamp)
        return hashMapOf("secret" to secret,"nonce" to nonce,"timestamp" to timestamp,"signature" to signature)
    }
}