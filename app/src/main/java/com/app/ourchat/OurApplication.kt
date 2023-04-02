package com.app.ourchat

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.app.ourchat.utils.Constant
import io.rong.imlib.RongIMClient

class OurApplication : Application() {

    companion object{
        lateinit var appContext:Context
    }


    override fun onCreate() {
        super.onCreate()
        appContext = this
        RongIMClient.init(this, Constant.rongCloudAppKey, true)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}