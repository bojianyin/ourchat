package com.app.ourchat.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.app.ourchat.network.model.MainModel
import com.app.ourchat.utils.EventBusUtil
import com.app.ourchat.utils.MessageEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseActivity : AppCompatActivity() {

    val TAG:String = this::class.java.simpleName
    private val disposables by lazy { CompositeDisposable() }

    val mainModel by lazy { MainModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())
        EventBusUtil.register(this)

        initView()
    }

    @LayoutRes
    abstract fun getContentView():Int

    open fun initView() {

    }


    fun addDisposable(disposable: Disposable?){
        if(disposable!=null){
            disposables.add(disposable)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBusUtil.unRegister(this)
        disposables.clear()

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(message:MessageEvent){

    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    open fun onStickyMessageEvent(message:MessageEvent){

    }


}