package com.app.ourchat.ui.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.app.ourchat.utils.EventBusUtil
import com.app.ourchat.utils.MessageEvent

class ConnectReceiver : BroadcastReceiver() {

    companion object {
        const val CONNEDTED = "com.app.ourchat.im.connected"
        const val DBOPEN = "com.app.ourchat.im.db.open"
        const val TOKEN_incorrect = "com.app.ourchat.im.token.incorrect"
        const val SERVICE_DES = "com.app.ourchat.im.service.destory"
    }

    override fun onReceive(context: Context, intent: Intent) {

        when(intent.action){
            CONNEDTED -> {
                EventBusUtil.postStickyMessage(MessageEvent(MessageEvent.msg_connect_success))
            }

            DBOPEN -> {
                EventBusUtil.postStickyMessage(MessageEvent(MessageEvent.msg_db_open))
            }
            TOKEN_incorrect -> {
                EventBusUtil.postStickyMessage(MessageEvent(MessageEvent.msg_token_incorrect))
            }
            SERVICE_DES -> {
                EventBusUtil.postStickyMessage(MessageEvent(MessageEvent.msg_service_destory))
            }
        }
    }
}