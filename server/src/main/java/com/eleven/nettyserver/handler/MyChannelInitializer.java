package com.eleven.nettyserver.handler;

import com.eleven.netty.codec.ProtoStuffWyMessageDecoder;
import com.eleven.netty.codec.ProtoStuffWyMessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

/********************************************************************
 * Created by Intellij IDEA.
 * @version 0.1
 * @date 2023/2/23 15:19
 * @author stz
 ********************************************************************/
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4))
                .addLast(new LengthFieldPrepender(4,false))
                .addLast(new ProtoStuffWyMessageDecoder())
                .addLast(new ProtoStuffWyMessageEncoder())
                .addLast(new IdleStateHandler(0,0,15))
                .addLast(new HeartBeatHandler())
                .addLast(new AuthHandler())
                .addLast(new PrintHandler())
                .addLast(new SyncTransferHandler())
                .addLast(new AsynTransferHandler());
    }
}
