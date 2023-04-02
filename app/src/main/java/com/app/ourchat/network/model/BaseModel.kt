package com.app.ourchat.network.model

import com.app.ourchat.network.HttpHelper
import com.app.ourchat.network.RetrofitHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

open class BaseModel {
    val mHttp by lazy { HttpHelper.instance.httpClient }

    val mRetrofitHelper by lazy { RetrofitHelper.instance }


    fun <T> changeUIThread(observable: Observable<T>,consumer:DisposableObserver<T>) : Disposable{
        return observable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeWith(consumer)
    }

}