package com.app.ourchat.utils;

public class MessageEvent {
    private int msgType;
    private Object content;

    public static final int msg_received_chat = 1;
    public static final int msg_connect_success = 2;
    public static final int msg_db_open = 3;
    public static final int msg_token_incorrect = 3;


    public MessageEvent(){

    }
    public MessageEvent(int type){
        this.msgType = type;
    }
    public MessageEvent(int type,Object content){
        this.msgType = type;
        this.content = content;
    }

    public int getMsgType() {
        return msgType;
    }

    public Object getContent() {
        return content;
    }
}
