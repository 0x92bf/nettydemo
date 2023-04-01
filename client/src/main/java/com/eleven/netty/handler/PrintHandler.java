package com.eleven.netty.handler;

import com.eleven.netty.entity.SyncMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/********************************************************************
 * Created by Intellij IDEA.
 * @version 0.1
 * @date 2023/2/24 14:00
 * @author stz
 ********************************************************************/
@Slf4j
public class PrintHandler extends SimpleChannelInboundHandler<SyncMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, SyncMessage syncMessage) throws Exception {
        log.info("打印消息:" + syncMessage.toString());
        channelHandlerContext.fireChannelRead(syncMessage);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

}
