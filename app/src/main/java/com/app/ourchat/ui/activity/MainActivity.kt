package com.app.ourchat.ui.activity

import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.app.ourchat.R
import com.app.ourchat.base.BaseActivity
import com.app.ourchat.network.bean.IMTokenBean
import com.app.ourchat.network.model.BaseResponseObserver
import com.app.ourchat.utils.DeviceUtil
import com.app.ourchat.utils.IMUtil
import com.app.ourchat.utils.SPUtil
import io.rong.imlib.RongCoreClient
import io.rong.imlib.RongIMClient
import io.rong.imlib.listener.OnReceiveMessageWrapperListener
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import io.rong.imlib.model.ReceivedProfile


class MainActivity : BaseActivity() {

    val tvSend: TextView by lazy { findViewById(R.id.tv_send) }

    override fun getContentView(): Int = R.layout.activity_main


    override fun initView() {
        super.initView()

        initToken()

        tvSend.setOnClickListener {
            IMUtil.sendMsg("王八蛋")
        }
    }

    fun initToken(){
        val token = SPUtil.getString(SPUtil.TOKEN)
        if(TextUtils.isEmpty(token)){
            val uuid = DeviceUtil.getUUID()
            mainModel.getToken(uuid,"",consumer = object :BaseResponseObserver<IMTokenBean>(){
                override fun onSuccess(result: IMTokenBean) {
                    Log.d(TAG,result.token)
                    if(result.code==200){
                        SPUtil.putString(SPUtil.TOKEN,result.token)
                        initConnect(result.token)
                    }else{
                        Toast.makeText(this@MainActivity,"token初始化失败",Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }else{
            initConnect(token)
        }
    }

    fun initConnect(token:String){
        RongIMClient.connect(token,5,MyConnectCallback())
    }


    inner class MyConnectCallback : RongIMClient.ConnectCallback(){
        override fun onSuccess(t: String?) {
            //连接成功，如果 onDatabaseOpened() 时没有页面跳转，也可在此时进行跳转。

            RongCoreClient.addOnReceiveMessageListener(listener)
            getHistoryMessage()
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
            } else {
                //数据库打开失败，可以弹出 toast 提示。
            }
        }

    }

    val listener = object : OnReceiveMessageWrapperListener() {
        override fun onReceivedMessage(message: Message?, profile: ReceivedProfile?) {
            Log.d(TAG,message?.content?.toString() ?: "null")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RongCoreClient.removeOnReceiveMessageListener(listener)
    }


    fun getHistoryMessage(){
        val conversationType: Conversation.ConversationType = Conversation.ConversationType.PRIVATE
        val targetId = IMUtil.getTargetId()
        val oldestMessageId = -1
        val count = 10

        RongIMClient.getInstance()
            .getHistoryMessages(conversationType, targetId, oldestMessageId, count,
                object : RongIMClient.ResultCallback<List<Message?>?>() {
                    /**
                     * 成功时回调
                     * @param messages 获取的消息列表
                     */
                    override fun onSuccess(messages: List<Message?>?) {
                        Log.d(TAG,messages?.size?.toString()?:"0")
                    }

                    /**
                     * 错误时回调。
                     * @param e 错误码
                     */
                    override fun onError(e: RongIMClient.ErrorCode) {

                    }
                })
    }
}