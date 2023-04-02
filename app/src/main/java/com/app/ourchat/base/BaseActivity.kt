package com.app.ourchat.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.app.ourchat.network.model.MainModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseActivity : AppCompatActivity() {

    val TAG:String = this::class.java.simpleName
    private val disposables by lazy { CompositeDisposable() }

    val mainModel by lazy { MainModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(getContentView())

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
        disposables.clear()
    }


}