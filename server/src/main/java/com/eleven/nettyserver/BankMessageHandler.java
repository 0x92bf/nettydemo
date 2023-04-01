package com.eleven.nettyserver;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketCloseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@ChannelHandler.Sharable
@Slf4j
@Component
public class BankMessageHandler extends SimpleChannelInboundHandler<Object> {

    @Autowired
    BankService bankService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof TextWebSocketFrame) {
            //TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) msg;
            // 业务层处理数据
            //this.bankService.transferMoney(textWebSocketFrame.text());
            // 响应客户端

            ctx.channel().writeAndFlush("hahaha");

        } else {
            log.info("异常");
            // 不接受文本以外的数据帧类型
            ctx.channel().writeAndFlush(WebSocketCloseStatus.INVALID_MESSAGE_TYPE).addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.info("链接断开：{}", ctx.channel().remoteAddress());
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ChannelMap.getInstance().addChannel("abc",ctx.channel());
        log.info("链接创建：{}{}", ctx.channel().remoteAddress(),ctx.name());
    }


}
