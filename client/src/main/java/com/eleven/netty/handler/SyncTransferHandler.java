package com.eleven.netty.handler;

import com.eleven.netty.common.MsgConstants;
import com.eleven.netty.entity.MessageData;
import com.eleven.netty.entity.SyncMessage;
import com.eleven.HandleMsgService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SyncTransferHandler extends SimpleChannelInboundHandler<SyncMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, SyncMessage serverMessage) {
        if(MsgConstants.MsgType.SERVER_TRANSFER == serverMessage.getType()){
            log.info("接收服务端消息，并响应：" + serverMessage);

            serverMessage.getBody().setDataCode(MessageData.DataCode.RESPONSE);
            HandleMsgService.doHandle(serverMessage);
            channelHandlerContext.channel().writeAndFlush(serverMessage);

        }else if(MsgConstants.MsgType.CLIENT_TRANSFER == serverMessage.getType()){
            log.info("接收服务端响应消息:" + serverMessage);

            MsgConstants.Transfer.AsycMsg asycMsg = MsgConstants.Transfer.syncMsgCountDownLatchMap.get(serverMessage.getMsgId());
            asycMsg.setResult(serverMessage.getBody());
            asycMsg.getCountDownLatch().countDown();

        }else{
            channelHandlerContext.fireChannelRead(serverMessage);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

}
