package com.app.ourchat

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.app.ourchat.utils.*
import io.rong.imlib.RongCoreClient
import io.rong.imlib.listener.OnReceiveMessageWrapperListener
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import io.rong.imlib.model.ReceivedProfile
import io.rong.message.TextMessage
import io.rong.push.PushEventListener
import io.rong.push.PushType
import io.rong.push.RongPushClient
import io.rong.push.notification.PushNotificationMessage
import io.rong.push.pushconfig.PushConfig

class OurApplication : Application(), PushEventListener {

    private val messageListener = object : OnReceiveMessageWrapperListener() {
        override fun onReceivedMessage(message: Message?, profile: ReceivedProfile?) {
            if(message?.content is TextMessage){
                val textMessage = message.content as TextMessage
                NotificationUtil.pushNotification(appContext, IMUtil.getOtherSideNick(),textMessage.content)
            }
            EventBusUtil.postMessage(MessageEvent(MessageEvent.msg_received_chat,message))
        }
    }

    companion object{
        lateinit var appContext:Context
    }


    override fun onCreate() {
        super.onCreate()
        appContext = this

        RongCoreClient.init(this, Constant.rongCloudAppKey,true)
        RongCoreClient.addOnReceiveMessageListener(messageListener)

        val build = PushConfig.Builder().enableRongPush(true).build()
        RongPushClient.setPushConfig(build)
        RongPushClient.setPushEventListener(this)
        RongPushClient.init(this, Constant.rongCloudAppKey)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    fun removeMessageListener(){
        RongCoreClient.removeOnReceiveMessageListener(messageListener)
    }

    override fun preNotificationMessageArrived(
        context: Context?,
        pushType: PushType?,
        notificationMessage: PushNotificationMessage?
    ): Boolean {
        notificationMessage?.let {
            val message = Message.obtain(
                it.targetId,
                Conversation.ConversationType.setValue(it.conversationType.value),
                TextMessage(it.pushContent)
            )
            message.targetId = it.targetId
            message.senderUserId = it.senderId
            EventBusUtil.postMessage(MessageEvent(MessageEvent.msg_received_chat,message))
        }

        return false
    }

    override fun afterNotificationMessageArrived(
        context: Context?,
        pushType: PushType?,
        notificationMessage: PushNotificationMessage?
    ) {

    }

    override fun onNotificationMessageClicked(
        context: Context?,
        pushType: PushType?,
        notificationMessage: PushNotificationMessage?
    ): Boolean {

        return false
    }

    override fun onThirdPartyPushState(pushType: PushType?, action: String?, resultCode: Long) {

    }

    override fun onTokenReceived(pushType: PushType?, token: String?) {

    }

    override fun onTokenReportResult(
        reportType: PushType?,
        code: Int,
        finalType: PushType?,
        finalToken: String?
    ) {

    }

}