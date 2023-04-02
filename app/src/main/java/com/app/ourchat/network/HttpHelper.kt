package com.app.ourchat.network

import com.app.ourchat.network.interceptor.HttpInterceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import java.util.*
import java.util.concurrent.TimeUnit

class HttpHelper private constructor(){

    var httpClient:OkHttpClient? = null

    init {

        initHttpClient()


    }

    fun initHttpClient(){
        if(httpClient == null){
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClient = OkHttpClient.Builder()
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .addInterceptor(interceptor)
                .connectTimeout(5*1000L,TimeUnit.MILLISECONDS)
                .readTimeout(5*1000L,TimeUnit.MILLISECONDS)
                .writeTimeout(5*1000L,TimeUnit.MILLISECONDS)
                .addInterceptor(HttpInterceptor())
                .build()
        }
    }

    companion object{
        val instance:HttpHelper by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
            HttpHelper()
        }
    }
}