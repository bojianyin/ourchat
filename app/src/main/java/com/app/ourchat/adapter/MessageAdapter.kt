package com.app.ourchat.adapter

import com.app.ourchat.R
import com.app.ourchat.utils.IMUtil
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.rong.imlib.model.Message
import io.rong.imlib.model.MessageContent
import io.rong.message.TextMessage
import org.jetbrains.annotations.NotNull


class MessageAdapter : BaseDelegateMultiAdapter<Message?, BaseViewHolder>(),LoadMoreModule {
    private val self_layout_type = 0
    private val other_layout_type = 1

    init {
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<Message?>() {
            override fun getItemType(@NotNull data: List<Message?>, position: Int): Int {
                val message = data[position]
                return if(IMUtil.isSelf(message?.senderUserId)) self_layout_type else other_layout_type
            }
        })
        getMultiTypeDelegate()?.run {
            addItemType(self_layout_type, R.layout.item_layout_self_message)
            addItemType(other_layout_type, R.layout.item_layout_message)
        }
    }

    override fun convert(holder: BaseViewHolder, item: Message?) {
        item?.run{
            holder.setText(R.id.tv_message,getContentByMessage(content))
            holder.setText(R.id.tv_send_person,
                if(IMUtil.isSelf(this.senderUserId)){
                    "我"
                }else{
                    "${IMUtil.getOtherSideNick()}"
                }
            )
        }

    }

    fun getContentByMessage(message: MessageContent):String{
        if(message is TextMessage){
            return message.content
        }else{
            return ""
        }

    }

}