package com.app.ourchat.utils

import org.greenrobot.eventbus.EventBus

class EventBusUtil {

    companion object {

        fun postMessage(msg:MessageEvent){
            EventBus.getDefault().post(msg)
        }

        fun postStickyMessage(msg:MessageEvent){
            EventBus.getDefault().postSticky(msg)
        }

        fun removeAllSticky(){
            EventBus.getDefault().removeAllStickyEvents()
        }
        fun register(subscriber:Any){
            if(!EventBus.getDefault().isRegistered(subscriber)){
                EventBus.getDefault().register(subscriber)
            }

        }

        fun unRegister(subscriber:Any){
            if(EventBus.getDefault().isRegistered(subscriber)){
                EventBus.getDefault().unregister(subscriber)
            }
        }
    }
}