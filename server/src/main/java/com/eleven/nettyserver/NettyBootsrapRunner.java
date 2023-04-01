package com.eleven.nettyserver;

import com.eleven.nettyserver.handler.*;
import com.eleven.netty.codec.ProtoStuffWyMessageDecoder;
import com.eleven.netty.codec.ProtoStuffWyMessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;



@Slf4j
@Component
public class NettyBootsrapRunner implements ApplicationRunner, ApplicationListener<ContextClosedEvent>, ApplicationContextAware {


    @Value("${netty.websocket.port}")
    private int port;

    @Value("${netty.websocket.ip}")
    private String ip;

    @Value("${netty.websocket.path}")
    private String path;

    @Value("${netty.websocket.max-frame-size}")
    private int maxFrameSize;

    private ApplicationContext applicationContext;

    private Channel serverChannel;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void run(ApplicationArguments args) throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            //serverBootstrap.localAddress(new InetSocketAddress(this.ip, this.port));
            serverBootstrap.localAddress(this.port);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
                    pipeline.addLast(new LengthFieldPrepender(4,false));
                    pipeline.addLast(new ProtoStuffWyMessageDecoder());
                    pipeline.addLast(new ProtoStuffWyMessageEncoder());
                    pipeline.addLast(new IdleStateHandler(0,0,15));
                    pipeline.addLast(applicationContext.getBean(AuthHandler.class));
                    //pipeline.addLast(new AuthHandler());
                    pipeline.addLast(new HeartBeatHandler());
                    pipeline.addLast(new PrintHandler());
                    pipeline.addLast(new SyncTransferHandler());
                    pipeline.addLast(new AsynTransferHandler());

                    /**
                     * 从IOC中获取到Handler
                     */
                    //pipeline.addLast(applicationContext.getBean(BankMessageHandler.class));
                }
            });
            Channel channel = serverBootstrap.bind().sync().channel();
            this.serverChannel = channel;
            log.info("socket 服务启动，ip={},port={}", this.ip, this.port);
            //获取Channel的closeFuture，并阻塞当前线程，直到关闭，这样可以在finally里执行一些资源回收的后续处理。
            channel.closeFuture().sync();
            //向jvm 注册一个shutdown的钩子事件，确保netty正常安全退出释放资源
//            Runtime.getRuntime().addShutdownHook(new Thread(()->{
//                try {
//                    bossGroup.shutdownGracefully();
//                    workerGroup.shutdownGracefully();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }));
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void onApplicationEvent(ContextClosedEvent event) {
        if (this.serverChannel != null) {
            this.serverChannel.close();
        }
        log.info("websocket 服务停止");
    }
}
