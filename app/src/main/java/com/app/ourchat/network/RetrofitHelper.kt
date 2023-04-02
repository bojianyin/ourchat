package com.app.ourchat.network

import io.reactivex.internal.schedulers.RxThreadFactory
import io.reactivex.plugins.RxJavaPlugins
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper private constructor(){

    private var retrofit:Retrofit? = null

    private val client by lazy { HttpHelper.instance.httpClient }

    private var serviceMap = hashMapOf<String,Any>()

    var baseUrl = "http://api-cn.ronghub.com/"

    init {
        initRetrofit()
    }

    private fun initRetrofit() {
        if(retrofit==null){
            retrofit = Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build()
        }
    }

    fun <T> createService(serviceClass:Class<T>) : T {
        if(retrofit==null) initRetrofit()
        val name = serviceClass.name
        return if(serviceMap.containsKey(name)){
            serviceMap[name] as T
        }else{
            val service = retrofit!!.create(serviceClass)
            serviceMap[name] = service as Any
            service
        }
    }

    companion object{
        val instance:RetrofitHelper by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
            RetrofitHelper()
        }
    }
}