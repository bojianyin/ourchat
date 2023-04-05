package com.app.ourchat.utils

import android.widget.Toast
import com.app.ourchat.OurApplication

object ToastUtil {
    fun show(msg:String){
        Toast.makeText(OurApplication.appContext,msg,Toast.LENGTH_SHORT).show()
    }
}