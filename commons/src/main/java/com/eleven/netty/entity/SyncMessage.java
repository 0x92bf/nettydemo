package com.eleven.netty.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

/********************************************************************
 * Created by Intellij IDEA.
 * @version 0.1
 * @date 2023/2/23 16:09
 * @author stz
 ********************************************************************/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SyncMessage {

    private String msgId;

    private String enterpriseCode;

    /**
     * 消息类型
     */
    private byte type;

    /**
     * 携带数据
     */
    private MessageData body;

    public SyncMessage(byte type, MessageData.DataCode dataCode) {
        this.msgId = UUID.randomUUID().toString();
        this.type = type;
        this.body = new MessageData(dataCode);
    }

    public SyncMessage(byte type, MessageData.DataCode dataCode, Object data) {
        this.msgId = UUID.randomUUID().toString();
        this.type = type;
        this.body = new MessageData(dataCode, data);
    }

}
