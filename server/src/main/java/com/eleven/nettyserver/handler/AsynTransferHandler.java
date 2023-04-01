package com.eleven.nettyserver.handler;

import com.eleven.netty.common.MsgConstants;
import com.eleven.netty.entity.MessageData;
import com.eleven.netty.entity.SyncMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AsynTransferHandler extends SimpleChannelInboundHandler<SyncMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, SyncMessage clientMessage) {
        if(MsgConstants.MsgType.ASYN_TRANSFER == clientMessage.getType()){
            log.info("接收客户端消息：" + clientMessage);
            sqlHandler(clientMessage.getBody());
        }else{
            //这里是最后一个处理器，先抛异常，后续增加处理器时需要放行此处消息
            //channelHandlerContext.fireChannelRead(clientMessage);
            throw new RuntimeException("No processor processing this message!");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }


    /**
     *
     * @param messageData
     */
    private void sqlHandler(MessageData messageData){
        if(MessageData.DataCode.SQL.toString().equals(messageData.getDataCode())){
            log.info("打印sql:{}",messageData.getData());
        }
    }
}
