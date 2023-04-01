package com.eleven.nettyserver.handler;

import com.eleven.nettyserver.ChannelMap;
import com.eleven.netty.common.MsgConstants;
import com.eleven.netty.entity.SyncMessage;
import com.eleven.netty.entity.SyncMessageBuilder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/********************************************************************
 * Created by Intellij IDEA.
 * @version 0.1
 * @date 2023/2/24 14:56
 * @author stz
 ********************************************************************/
@Slf4j
public class HeartBeatHandler extends SimpleChannelInboundHandler<SyncMessage> {
    ChannelMap channelManager = ChannelMap.getInstance();

    /**
     * 处理心跳包回复
     * @param ctx
     * @param wyMessage
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SyncMessage wyMessage) throws Exception {
        String id = ctx.channel().id().asLongText();
        if(wyMessage.getType() == MsgConstants.MsgType.HEART_MSG){
            log.info("收到ip:"+ctx.channel().remoteAddress().toString()+" id:"+id+" 心跳回应消息:"+wyMessage.getBody().getDataCode());
            if(channelManager.getHeartMap().containsKey(id)){
                channelManager.getHeartMap().remove(id);
            }
        }else {
            ctx.fireChannelRead(wyMessage);
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
        String id = ctx.channel().id().asLongText();
        AtomicInteger atomicInteger = channelManager.getHeartMap().get(id);
        if(atomicInteger != null){

            int n = atomicInteger.getAndIncrement();
            if(n>3){
                channelManager.removeChannel(id);
                log.error("ip:"+ctx.channel().remoteAddress().toString()+" id:"+id+"心跳无响应，被剔除");
                ctx.close();
                return;
            }
        }else {
            channelManager.getHeartMap().put(id,new AtomicInteger(1));
        }
        log.info("ip:"+ctx.channel().remoteAddress().toString()+"id:"+id+"第"+channelManager.getHeartMap().get(id)+"发送心跳信息");
        ctx.writeAndFlush(SyncMessageBuilder.pingBuild());

    }

}
