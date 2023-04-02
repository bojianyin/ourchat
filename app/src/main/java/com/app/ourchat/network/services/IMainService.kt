package com.app.ourchat.network.services

import com.app.ourchat.network.bean.IMTokenBean
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface IMainService {

    @FormUrlEncoded
    @POST("user/getToken.json")
    fun getToken(@Field("userId") userId:String,@Field("name") name:String) : Observable<IMTokenBean>


//    @POST("user/whitesetting/query.json")
//    fun getUserList(@Field("userId"))
}