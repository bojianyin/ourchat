package com.app.ourchat.network.model

import com.app.ourchat.network.bean.IMTokenBean
import com.app.ourchat.network.services.IMainService
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver

class MainModel :BaseModel() {
    fun getToken(userId:String,name:String,consumer:DisposableObserver<IMTokenBean>):Disposable{
        return changeUIThread(mRetrofitHelper.createService(IMainService::class.java).getToken(userId,name),consumer)
    }
}