package com.eleven;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ChannelHandler.Sharable
public class BankHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Client,channelActive");
        ctx.channel().writeAndFlush("111111111");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("rrwwww");
        //if (msg instanceof TextWebSocketFrame) {
           // TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) msg;
           log.info((String) msg);
            //ctx.channel().writeAndFlush(new TextWebSocketFrame("我收到了你的消息：" + System.currentTimeMillis()));

//        } else {
//            // 不接受文本以外的数据帧类型
//            ctx.channel().writeAndFlush(WebSocketCloseStatus.INVALID_MESSAGE_TYPE).addListener(ChannelFutureListener.CLOSE);
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("Client,exceptionCaught");
        cause.printStackTrace();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("Client,channelInactive");

    }

}
