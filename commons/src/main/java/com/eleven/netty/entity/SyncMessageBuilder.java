package com.eleven.netty.entity;

import com.eleven.netty.common.MsgConstants;

/*********************************
 * Created by IntelliJ IDEA.
 * @Author : stz
 * @create 2023/3/24 9:41
 *********************************/
public class SyncMessageBuilder {

    public static SyncMessage pingBuild(){
        return new SyncMessage(MsgConstants.MsgType.HEART_MSG, MessageData.DataCode.PING);
    }

    public static SyncMessage pongBuild(){
        return new SyncMessage(MsgConstants.MsgType.HEART_MSG, MessageData.DataCode.PONG);
    }

    /**
     * 异步消息
     * @param data
     * @return
     */
    public static SyncMessage buildMsg(MessageData.DataCode dataCode, Object data){
        return buildMsg(MsgConstants.MsgType.ASYN_TRANSFER, dataCode, data);
    }

    /**
     * 同步消息
     * @param data
     * @param msgCode
     * @return
     */
    public static SyncMessage buildMsg(byte msgCode, MessageData.DataCode dataCode, Object data){
        return new SyncMessage(msgCode, dataCode, data);
    }

}
