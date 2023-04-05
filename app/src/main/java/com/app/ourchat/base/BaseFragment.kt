package com.app.ourchat.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.app.ourchat.utils.EventBusUtil
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseFragment :Fragment() {

    var rootLayout:View? = null

    var mUserVisible:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBusUtil.register(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(rootLayout==null){
            rootLayout = inflater.inflate(getContentView(),container,false)
            loadData()
        }else{
            val viewGroup = rootLayout?.parent as ViewGroup
            viewGroup.removeAllViews()
        }

        return rootLayout
    }

    fun loadData(){

    }

    fun fragmentVisible(visible:Boolean){
        mUserVisible = visible
    }

    fun initView(){

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        fragmentVisible(isVisibleToUser)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        fragmentVisible(!hidden)
    }

    override fun onResume() {
        super.onResume()
        if(userVisibleHint){
            fragmentVisible(userVisibleHint)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBusUtil.unRegister(this)
    }

    @LayoutRes
    abstract fun getContentView():Int

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(){

    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    fun onStickyMessageEvent(){

    }

}