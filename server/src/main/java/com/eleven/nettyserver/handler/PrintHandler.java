package com.eleven.nettyserver.handler;

import com.eleven.netty.entity.SyncMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/********************************************************************
 * Created by Intellij IDEA.
 * @version 0.1
 * @date 2023/2/23 15:29
 * @author stz
 ********************************************************************/
@Slf4j
public class PrintHandler extends SimpleChannelInboundHandler<SyncMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, SyncMessage wyMessage) throws Exception {
        //if(wyMessage.getType() == MsgConstants.MsgType.SQL_MSG){
            log.info("打印消息:" + wyMessage.toString());
            channelHandlerContext.fireChannelRead(wyMessage);
        //}
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
