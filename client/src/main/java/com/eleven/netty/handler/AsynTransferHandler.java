package com.eleven.netty.handler;

import com.eleven.netty.common.MsgConstants;
import com.eleven.netty.entity.SyncMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AsynTransferHandler extends SimpleChannelInboundHandler<SyncMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, SyncMessage serverMessage) {
        if(MsgConstants.MsgType.ASYN_TRANSFER == serverMessage.getType()){
            log.info("接收服务端消息：" + serverMessage);

        }else{
            //这里是最后一个处理器，先抛异常，后续增加处理器时需要放行此处消息
            //channelHandlerContext.fireChannelRead(serverMessage);
            throw new RuntimeException("No processor processing this message!");
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
