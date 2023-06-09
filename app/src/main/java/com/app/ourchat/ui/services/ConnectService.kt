package com.app.ourchat.ui.services
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.app.ourchat.ui.broadcast.ConnectReceiver
import com.app.ourchat.utils.EventBusUtil
import com.app.ourchat.utils.MessageEvent
import com.app.ourchat.utils.NotificationUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.rong.imlib.IRongCoreListener
import io.rong.imlib.RongCoreClient
import io.rong.imlib.RongIMClient
import java.util.*
import java.util.concurrent.TimeUnit

class ConnectService : Service(), IRongCoreListener.ConnectionStatusListener {

    var connectToken = ""

    private var disposable:Disposable? = null
    companion object {
        var TOKEN = "token"
        var isConnectService:Boolean = false
        var isOpenNativeDB = false

        fun openService(context:Context, token:String){
            //启动服务
            val intent = Intent(context, ConnectService::class.java)
            intent.putExtra(TOKEN,token)
            // Android 8.0使用startForegroundService在前台启动新服务
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                context.startForegroundService(intent)
            }else{
                context.startService(intent)
            }

        }

    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("ConnectService","onCreate>>>")
        NotificationUtil.startForegroundWithNotification(this)

        RongCoreClient.setConnectionStatusListener(this)

        disposable = Observable.interval(5000L,5000L,TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("ConnectService","whileing into...")
                if(isConnectService) return@subscribe
                connectIM()
            }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(isConnectService) {

//            sendMyBroadcast(ConnectReceiver.CONNEDTED)
            EventBusUtil.postMessage(MessageEvent(MessageEvent.msg_connect_success))
            if(isOpenNativeDB) {
//                sendMyBroadcast(ConnectReceiver.DBOPEN)
                EventBusUtil.postMessage(MessageEvent(MessageEvent.msg_db_open))
            }
            return super.onStartCommand(intent, flags, startId)
        }
        intent?.run {
            connectToken = getStringExtra(TOKEN) ?: ""
            connectIM()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    fun connectIM(){
        if("".equals(connectToken)) return
        RongIMClient.connect(connectToken,5,MyConnectCallback())
    }


    override fun onDestroy() {
        super.onDestroy()
        isConnectService = false
        EventBusUtil.postStickyMessage(MessageEvent(MessageEvent.msg_service_destory))
        disposable?.dispose()
//        RongIMClient.getInstance().disconnect()
        RongCoreClient.removeConnectionStatusListener(this)
        stopSelf()
    }

    inner class MyConnectCallback : RongIMClient.ConnectCallback(){
        override fun onSuccess(t: String?) {
            //连接成功，如果 onDatabaseOpened() 时没有页面跳转，也可在此时进行跳转。
            isConnectService = true


//            sendMyBroadcast(ConnectReceiver.CONNEDTED)
            EventBusUtil.postMessage(MessageEvent(MessageEvent.msg_connect_success))

        }

        override fun onError(e: RongIMClient.ConnectionErrorCode?) {
            e?.run {
                if(equals(RongIMClient.ConnectionErrorCode.RC_CONN_TOKEN_EXPIRE)) {
                    //从 APP 服务请求新 token，获取到新 token 后重新 connect()
                    stopSelf()
                } else if (equals(RongIMClient.ConnectionErrorCode.RC_CONNECT_TIMEOUT)) {
                    //连接超时，弹出提示，可以引导用户等待网络正常的时候再次点击进行连接
                } else if(equals(RongIMClient.ConnectionErrorCode.RC_CONNECTION_EXIST)){
                    //连接已经存在，不需要重复连接。
                    isConnectService = true

//                    sendMyBroadcast(ConnectReceiver.CONNEDTED)
                    EventBusUtil.postMessage(MessageEvent(MessageEvent.msg_connect_success))
                }else{

                }
            }

        }

        override fun onDatabaseOpened(code: RongIMClient.DatabaseOpenStatus?) {
            if(RongIMClient.DatabaseOpenStatus.DATABASE_OPEN_SUCCESS.equals(code)) {
                //本地数据库打开，跳转到会话列表页面
                isOpenNativeDB = true

//                sendMyBroadcast(ConnectReceiver.DBOPEN)
                EventBusUtil.postMessage(MessageEvent(MessageEvent.msg_db_open))
            } else {
                //数据库打开失败，可以弹出 toast 提示。
            }
        }

    }


    override fun onChanged(status: IRongCoreListener.ConnectionStatusListener.ConnectionStatus?) {
        when(status){
            IRongCoreListener.ConnectionStatusListener.ConnectionStatus.UNCONNECTED
                ,IRongCoreListener.ConnectionStatusListener.ConnectionStatus.NETWORK_UNAVAILABLE
                ,IRongCoreListener.ConnectionStatusListener.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT
                ,IRongCoreListener.ConnectionStatusListener.ConnectionStatus.CONN_USER_BLOCKED
                ,IRongCoreListener.ConnectionStatusListener.ConnectionStatus.SIGN_OUT
                ,IRongCoreListener.ConnectionStatusListener.ConnectionStatus.SUSPEND
                ,IRongCoreListener.ConnectionStatusListener.ConnectionStatus.TIMEOUT -> {

                isConnectService = false
                connectIM()

            }

            IRongCoreListener.ConnectionStatusListener.ConnectionStatus.TOKEN_INCORRECT -> {
                isConnectService = false
//                sendMyBroadcast(ConnectReceiver.TOKEN_incorrect)
                EventBusUtil.postMessage(MessageEvent(MessageEvent.msg_token_incorrect))

                stopSelf()

            }
            IRongCoreListener.ConnectionStatusListener.ConnectionStatus.CONNECTED -> {
                isConnectService = true
            }
            else -> {

            }
        }
    }

    fun sendMyBroadcast(action:String){
        val intent = Intent().apply {
            this.action = action
        }
        sendBroadcast(intent)
    }
}