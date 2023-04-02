package com.app.ourchat.utils

import android.content.Context
import android.content.SharedPreferences
import com.app.ourchat.OurApplication.Companion.appContext

object SPUtil {
    const val UUID:String = "uuid"
    const val TOKEN:String = "token"

    val applicationContext by lazy { appContext.applicationContext }

    val sharedPreferences: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            applicationContext.packageName + "_preferences",
            Context.MODE_PRIVATE
        )
    }



    fun putString(key:String,value:String){
        sharedPreferences.edit().putString(key,value).commit()
    }

    fun getString(key:String):String{
        return sharedPreferences.getString(key,"") ?: ""
    }

}