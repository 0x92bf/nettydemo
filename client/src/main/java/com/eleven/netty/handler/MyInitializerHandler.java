package com.eleven.netty.handler;

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
 * @date 2023/2/24 11:42
 * @author stz
 ********************************************************************/
public class MyInitializerHandler extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        /**
         * LengthFieldBasedFrameDecoder和 LengthFieldPrepender 解决粘包、拆包的问题
         * ProtoStuffWyMessageDecoder 和 ProtoStuffWyMessageDecoder 用户将WyMessage和ByteBuf 的自动转换程序内直接对WyMessage操作即可
         */
        socketChannel.pipeline()
                //【解码】将数据前4个字节解析位长度，并丢弃前四个字节
                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4))
                //【编码】在ProtoStuffWyMessageEncoder编码后的字节数据，头部加4个字节，表示数据的长度（不包含头部的4个字节）
                .addLast(new LengthFieldPrepender(4,false))
                .addLast(new ProtoStuffWyMessageDecoder())
                .addLast(new ProtoStuffWyMessageEncoder())
                .addLast(new IdleStateHandler(0,0,30))
                .addLast(new AuthHandler())
                .addLast(new HeartBeatHandler())
                .addLast(new PrintHandler())
                .addLast(new SyncTransferHandler())
                .addLast(new AsynTransferHandler());
    }
}
