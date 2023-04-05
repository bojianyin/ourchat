package com.app.ourchat.ui.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.app.ourchat.utils.EventBusUtil
import com.app.ourchat.utils.MessageEvent
import io.rong.imlib.RongIMClient

class ConnectService : Service() {

    var connectToken = ""
    companion object {
        var TOKEN = "token"
        var isConnectService:Boolean = false

        fun openService(context:Context,token:String){
            val intent = Intent(context, ConnectService::class.java)
            intent.putExtra(TOKEN,token)
            context.startService(intent)
        }

    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("ConnectService","onCreate>>>")
        RongIMClient.setConnectionStatusListener {
            if(it.value == RongIMClient.ConnectionStatusListener.ConnectionStatus.UNCONNECTED.value){
                isConnectService = false
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(isConnectService) {
            EventBusUtil.postMessage(MessageEvent(MessageEvent.msg_connect_success))
            return super.onStartCommand(intent, flags, startId)
        }
        intent?.run {
            connectToken = getStringExtra(TOKEN) ?: ""
            RongIMClient.connect(connectToken,5,MyConnectCallback())
        }
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
    }

    inner class MyConnectCallback : RongIMClient.ConnectCallback(){
        override fun onSuccess(t: String?) {
            //连接成功，如果 onDatabaseOpened() 时没有页面跳转，也可在此时进行跳转。
            isConnectService = true

            EventBusUtil.postMessage(MessageEvent(MessageEvent.msg_connect_success))

        }

        override fun onError(e: RongIMClient.ConnectionErrorCode?) {
            e?.run {
                if(equals(RongIMClient.ConnectionErrorCode.RC_CONN_TOKEN_EXPIRE)) {
                    //从 APP 服务请求新 token，获取到新 token 后重新 connect()
                } else if (equals(RongIMClient.ConnectionErrorCode.RC_CONNECT_TIMEOUT)) {
                    //连接超时，弹出提示，可以引导用户等待网络正常的时候再次点击进行连接
                } else {
                    //其它业务错误码，请根据相应的错误码作出对应处理。
                }
            }

        }

        override fun onDatabaseOpened(code: RongIMClient.DatabaseOpenStatus?) {
            if(RongIMClient.DatabaseOpenStatus.DATABASE_OPEN_SUCCESS.equals(code)) {
                //本地数据库打开，跳转到会话列表页面
//                EventBusUtil.postMessage(MessageEvent(MessageEvent.msg_connect_success))
            } else {
                //数据库打开失败，可以弹出 toast 提示。
            }
        }

    }
}