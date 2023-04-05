package com.app.ourchat.ui.activity

import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.ourchat.R
import com.app.ourchat.adapter.MessageAdapter
import com.app.ourchat.base.BaseActivity
import com.app.ourchat.network.bean.IMTokenBean
import com.app.ourchat.network.model.BaseResponseObserver
import com.app.ourchat.ui.services.ConnectService
import com.app.ourchat.utils.*
import com.jaeger.library.StatusBarUtil
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message


class MainActivity : BaseActivity() {

    val tvSend: TextView by lazy { findViewById(R.id.tv_send) }
    val tvTitle: TextView by lazy { findViewById(R.id.tv_title) }
    val rvMsg: RecyclerView by lazy { findViewById(R.id.rv_msg) }
    val adapter:MessageAdapter by lazy { MessageAdapter() }
    val etMsg:EditText by lazy { findViewById(R.id.et_msg) }
    private var lyManager:LinearLayoutManager? = null

    override fun getContentView(): Int = R.layout.activity_main


    override fun initView() {
        super.initView()

        StatusBarUtil.setColor(this,ContextCompat.getColor(this,R.color.white),0)
        StatusBarUtil.setLightMode(this)
        tvTitle.text = "❤️❤️❤️ (连接中...)"
        lyManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,true)
        rvMsg.layoutManager = lyManager
        rvMsg.adapter = adapter

        adapter.loadMoreModule.setOnLoadMoreListener {
            Log.d(TAG,"loadMoreModule ....")
            val lastMessage = getLastMessage()
            val messageId = lastMessage?.messageId ?: return@setOnLoadMoreListener
            getHistoryMessage(messageId)
        }

        initToken()

        tvSend.setOnClickListener {
            val text = etMsg.text.toString()
            if("".equals(text)){
                return@setOnClickListener
            }

            val message = IMUtil.sendMsg(text)
            message.senderUserId = IMUtil.currentUid
            etMsg.setText("")
            adapter.addData(0,message)
            lyManager?.scrollToPositionWithOffset(0,0)
        }
    }

    fun initToken(){
        val token = SPUtil.getString(SPUtil.TOKEN)
        if(TextUtils.isEmpty(token)){
            val uuid = DeviceUtil.getUUID()
            mainModel.getToken(uuid,"",consumer = object :BaseResponseObserver<IMTokenBean>(){
                override fun onSuccess(result: IMTokenBean) {
                    Log.d(TAG,result.token)
                    if(result.code==200){
                        SPUtil.putString(SPUtil.TOKEN,result.token)
                        initConnect(result.token)
                    }else{
                        Toast.makeText(this@MainActivity,"token初始化失败",Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }else{
            IMUtil.currentUid = SPUtil.getString(SPUtil.UUID)
            initConnect(token)
        }
    }

    fun initConnect(token:String) {
        ConnectService.openService(this,token)
    }


    fun getHistoryMessage(messageId:Int = -1){
        val conversationType: Conversation.ConversationType = Conversation.ConversationType.PRIVATE
        val targetId = IMUtil.getTargetId()
        val count = 30

        RongIMClient.getInstance()
            .getHistoryMessages(conversationType, targetId, messageId, count,
                object : RongIMClient.ResultCallback<List<Message?>?>() {
                    /**
                     * 成功时回调
                     * @param messages 获取的消息列表
                     */
                    override fun onSuccess(messages: List<Message?>?) {
                        Log.d(TAG,messages?.size?.toString()?:"0")
                        messages?.run{
                            if(size>0){
                                if(messageId == -1){
                                    adapter.setList(this)
                                } else {
                                    adapter.addData(adapter.data.size,this)
                                }
                                adapter.loadMoreModule.loadMoreComplete()
                                if(size<count){
                                    adapter.loadMoreModule.loadMoreEnd()
                                }
                            }else{
                                adapter.setList(null)
                                adapter.loadMoreModule.loadMoreEnd()
                            }
                        }
                    }

                    /**
                     * 错误时回调。
                     * @param e 错误码
                     */
                    override fun onError(e: RongIMClient.ErrorCode) {

                    }
                })
    }

    override fun onMessageEvent(message: MessageEvent) {
        super.onMessageEvent(message)
        when(message.msgType){
            MessageEvent.msg_received_chat -> {
                if(message.content!=null && message.content is Message){
                    val imMessage = message.content as Message
                    adapter.addData(0,imMessage)
                    lyManager?.scrollToPositionWithOffset(0,0)

                }
            }

            MessageEvent.msg_connect_success -> {
                tvTitle.text = "❤️❤️❤️"
                getHistoryMessage()
            }
        }

    }

    fun getLastMessage():Message? {
        if(adapter.data.size<=0) return null
        return adapter.data[adapter.data.size-1]
    }


    override fun onBackPressed() {
//        super.onBackPressed()
        val homeIntent = Intent(Intent.ACTION_MAIN)
        homeIntent.addCategory(Intent.CATEGORY_HOME)
        startActivity(homeIntent)
    }
}