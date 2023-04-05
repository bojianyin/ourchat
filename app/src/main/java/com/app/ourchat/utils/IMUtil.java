package com.app.ourchat.utils;

import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

public class IMUtil {

    public static String currentUid = "";



    //获取接收消息方id
    public static String getTargetId(){
        if("".equals(currentUid)) {
            final String cUid = SPUtil.INSTANCE.getString(SPUtil.UUID);
            currentUid = cUid;
        }

        if(currentUid.equals(Constant.userId1)){
            return Constant.userId2;
        }else{
            return Constant.userId1;
        }
    }

    public static boolean isSelf(String uid){
        return currentUid.equals(uid);
    }

    public static String getOtherSideNick(){
        if(DeviceUtil.getHardware().equals("kirin9000")){
            return "宝贝";
        }else{
            return "老公";
        }
    }

    //发送消息
    public static Message sendMsg(String msg){
        String targetId = getTargetId();
        Conversation.ConversationType conversationType = Conversation.ConversationType.PRIVATE;
        TextMessage messageContent = TextMessage.obtain(msg);

        Message message = Message.obtain(targetId, conversationType, messageContent);

        RongIMClient.getInstance().sendMessage(message, null, null, new IRongCallback.ISendMessageCallback() {

            @Override
            public void onAttached(Message message) {

            }

            @Override
            public void onSuccess(Message message) {

            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {

            }
        });
        return message;
    }
}
