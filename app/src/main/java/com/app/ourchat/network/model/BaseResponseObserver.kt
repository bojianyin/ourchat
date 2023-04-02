package com.app.ourchat.network.model

import io.reactivex.observers.DisposableObserver

abstract class BaseResponseObserver<T> : DisposableObserver<T>() {
    override fun onNext(t: T) {
        onSuccess(t)
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        onFailed()
    }

    override fun onComplete() {

    }

    abstract fun onSuccess(result:T)

    fun onFailed(){

    }
}