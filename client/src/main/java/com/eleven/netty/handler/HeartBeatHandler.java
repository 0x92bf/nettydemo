package com.eleven.netty.handler;


import com.eleven.netty.common.MsgConstants;
import com.eleven.netty.entity.SyncMessage;
import com.eleven.netty.entity.SyncMessageBuilder;
import com.eleven.netty.NettyClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/********************************************************************
 * 心跳处理handler
 * @version 0.1
 * @date 2023/2/24 15:01
 * @author stz
 ********************************************************************/
@Slf4j
public class HeartBeatHandler extends SimpleChannelInboundHandler<SyncMessage> {

    private AtomicInteger serverHeartbeat = new AtomicInteger(1);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SyncMessage syncMessage) throws Exception {

        serverHeartbeat.set(0);

        if(syncMessage.getType() == MsgConstants.MsgType.HEART_MSG){
            log.info("收到服务端{}心跳信息:{}",ctx.channel().remoteAddress(),syncMessage.getBody());
            SyncMessage response = SyncMessageBuilder.pongBuild();
            ctx.writeAndFlush(SyncMessageBuilder.pongBuild());
            log.info("回复服务端{}:{}",ctx.channel().remoteAddress(),response.getBody());
        }else {
            ctx.fireChannelRead(syncMessage);
        }
    }

    /**
     * 触发心跳周期，发送一条心跳信息
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        int andIncrement = serverHeartbeat.getAndIncrement();
        if(andIncrement>3){
            log.info("触发断线重连");
            NettyClient.instance().keepConnectionAlive();
        }else{
            log.info("第"+serverHeartbeat+"次向服务端发送心跳确认信息");
            ctx.writeAndFlush(SyncMessageBuilder.pingBuild());
        }

    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("触发断线重连:channelInactive");
        NettyClient.instance().keepConnectionAlive();
    }
}
