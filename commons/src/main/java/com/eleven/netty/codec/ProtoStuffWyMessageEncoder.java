package com.eleven.netty.codec;

import com.eleven.common.ProtoStuffUtil;
import com.eleven.netty.entity.SyncMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/********************************************************************
 * Created by Intellij IDEA.
 * @version 0.1
 * @date 2023/2/24 9:31
 * @author stz
 ********************************************************************/
public class ProtoStuffWyMessageEncoder extends MessageToMessageEncoder<SyncMessage> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, SyncMessage syncMessage, List<Object> list) throws Exception {

        if (syncMessage != null) {
            ByteBuf buf = Unpooled.buffer();
            buf.writeBytes(ProtoStuffUtil.serialize(syncMessage));
            list.add(buf);
        }
    }
}
