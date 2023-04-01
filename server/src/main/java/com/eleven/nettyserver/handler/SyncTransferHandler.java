package com.eleven.nettyserver.handler;

import com.eleven.netty.common.MsgConstants;
import com.eleven.netty.entity.MessageData;
import com.eleven.netty.entity.SyncMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SyncTransferHandler extends SimpleChannelInboundHandler<SyncMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, SyncMessage clientMessage) {
        if(MsgConstants.MsgType.CLIENT_TRANSFER == clientMessage.getType()){
            log.info("接收客户端消息，并响应：" + clientMessage);

            clientMessage.getBody().setDataCode(MessageData.DataCode.RESPONSE);
            clientMessage.setBody(new MessageData(MessageData.DataCode.REQUEST,"暂不考虑客户端主发请求"));
            channelHandlerContext.channel().writeAndFlush(clientMessage);

        }else if(MsgConstants.MsgType.SERVER_TRANSFER == clientMessage.getType()){
            log.info("接收客户端的响应消息" + clientMessage);

            MsgConstants.Transfer.AsycMsg asycMsg = MsgConstants.Transfer.syncMsgCountDownLatchMap.get(clientMessage.getMsgId());
            asycMsg.setResult(clientMessage.getBody());
            asycMsg.getCountDownLatch().countDown();
        }else {
            channelHandlerContext.fireChannelRead(clientMessage);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
