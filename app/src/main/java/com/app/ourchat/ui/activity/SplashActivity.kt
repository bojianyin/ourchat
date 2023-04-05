package com.app.ourchat.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.app.ourchat.R
import com.app.ourchat.base.BaseActivity
import com.jaeger.library.StatusBarUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SplashActivity : BaseActivity() {

    override fun getContentView(): Int {
        return R.layout.activity_splash
    }

    private fun hideBlackBar(activity: Activity){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            val lp = activity.getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            activity.getWindow().setAttributes(lp)
        }
    }

    override fun initView() {
        super.initView()
        StatusBarUtil.setColor(this,ContextCompat.getColor(this,R.color.white),0)
        StatusBarUtil.setLightMode(this)
//        StatusBarUtil.setTranslucent(this,0)
//        StatusBarUtil.hideFakeStatusBarView(this)
//        hideBlackBar(this)
        addDisposable(Observable.timer(1000L,TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            })
    }
}