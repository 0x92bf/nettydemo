package com.eleven.commons;

import com.eleven.netty.common.MsgConstants;
import com.eleven.netty.entity.MessageData;
import com.eleven.netty.entity.SyncMessage;
import com.eleven.netty.entity.SyncMessageBuilder;

public class ClientSyncMessageUtil {
    private static String enterpriseCode = SpringBeanUtil.getApplicationContext().getBean(EnterprisePM.class).getEnterpriseCode();

    public static SyncMessage buildMsg(MessageData.DataCode code, Object data) {
       return buildMsg(MsgConstants.MsgType.ASYN_TRANSFER, code, data);
    }

    public static SyncMessage buildMsg(byte type, MessageData.DataCode dataCode, Object data) {
        SyncMessage message = SyncMessageBuilder.buildMsg(type, dataCode, data);
        message.setEnterpriseCode(enterpriseCode);
        //message.setMsgId(UUID.randomUUID().toString());//此处ID标识可扩展

        return message;
    }
}
