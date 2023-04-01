package com.eleven.netty.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

/**
 * 消息类型
 * @author stz
 */
public class MsgConstants {


    /**
     * 消息类型
     */
    public class MsgType{

        /** 心跳消息 */
        public static final byte HEART_MSG = (byte)0;

        /** 登录消息 */
        public static final byte AUTH_MSG = (byte)1;

        /** 服务端发起：同步传输到客户端，并同时接收客户端响应 */
        public static final byte SERVER_TRANSFER = (byte)2;

        /** 客户端发起：同步传输到服务端，并同时接收服务端响应 */
        public static final byte CLIENT_TRANSFER = (byte)3;

        /** 异步传输，客户端与服务端均可发起 */
        public static final byte ASYN_TRANSFER = (byte)4;


    }


    /**
     * 同步消息 SERVER_TRANSFER/CLIENT_TRANSFER
     */
    public interface Transfer{
        ConcurrentMap<String,AsycMsg> syncMsgCountDownLatchMap = new ConcurrentHashMap();

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @ToString
        class AsycMsg {
            private CountDownLatch countDownLatch = new CountDownLatch(1);
            private Object result;
        }

        /** */
        public static final String INSERT = "INSERT";

    }


}
