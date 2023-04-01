package com.eleven.netty.codec;

import com.eleven.common.ProtoStuffUtil;
import com.eleven.netty.entity.SyncMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/********************************************************************
 * Created by Intellij IDEA.
 * @version 0.1
 * @date 2023/2/24 9:29
 * @author stz
 ********************************************************************/
public class ProtoStuffWyMessageDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte[] msgBody = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(msgBody);
        list.add(ProtoStuffUtil.deserialize(msgBody, SyncMessage.class));
    }
}
