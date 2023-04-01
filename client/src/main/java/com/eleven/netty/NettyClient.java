package com.eleven.netty;

import cn.hutool.json.JSONUtil;
import com.eleven.netty.handler.MyInitializerHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 创建netty客户端
 * @author stz
 */
@Slf4j
public class NettyClient {

    public Map<String, Channel> clientMap = new ConcurrentHashMap<String, Channel>();
    private Map<String, Map<String,Object>> clientConfigMap = new ConcurrentHashMap<String, Map<String,Object>>();
    private Map<String, Thread> shutdownThreadMap = new ConcurrentHashMap<>();
    private NettyClient(){};

    private static class SingletonHolder {
        private static final NettyClient NETTY_CLINT = new NettyClient();
    }

    public static NettyClient instance(){
        return SingletonHolder.NETTY_CLINT;
    }


    public Channel start(final String serverName, final String ip, final Integer port){
        return start0(serverName,ip,port,true);
    }


    /**
     * 启动客户端
     * 根据客户端名称保存客户端channel
     * @param serverName
     * @param ip
     * @param port
     * @param sync 是否同步阻塞启动
     */
    public synchronized Channel start0(final String serverName, final String ip, final Integer port, boolean sync){
        //检测是否已连接
        if(clientMap.containsKey(serverName) && clientMap.get(serverName).isActive()){
            log.info(serverName+"已经连接");
            return clientMap.get(serverName);
        }
        //改成对象 todo
        Map<String,Object> clientConfig = new HashMap<String, Object>();
        clientConfig.put("ip",ip);
        clientConfig.put("port",port);
        clientConfig.put("sync",sync);
        clientConfigMap.put(serverName,clientConfig);

        if(sync){
            Channel channel = null;
            AtomicLong n = new AtomicLong(0);
            do{
                log.info("[{}]第{}次尝试链接服务端",serverName,n.get());
                try{
                    channel = start0(serverName, ip, port);
                }catch (Exception e){
                    log.error("[{}]第{}次尝试链接服务端异常{}",serverName,n.get(),e.getMessage());
                }

                if(null == channel){
                    log.error("[{}]第{}次尝试链接服务端失败",serverName,n.get());
                }
                try {
                    if(n.get()>1){
                        TimeUnit.SECONDS.sleep(3);
                    }
                    n.incrementAndGet();
                } catch (InterruptedException e) { e.printStackTrace(); }
            }while (null == channel);
            log.info("[{}]成功连接到服务端,重试次数{}",serverName,n.get());
            return channel;
        }else{
            //线程池 todo
            new Thread(new Runnable() {
                @Override
                public void run() {
                    start1(serverName,ip,port);
                }
            }).start();
            return null;
        }
    }


    /**
     * 关闭客户端
     * 同时将该客户端从clinetMap中移除
     * @param serverName
     */
    public void close(String serverName){

        if(!clientMap.containsKey(serverName)){
            log.info(serverName+"不存在");
            return;
        }
        try {
            clientMap.get(serverName).close().sync();
            clientMap.remove(serverName);
            Runtime.getRuntime().removeShutdownHook(shutdownThreadMap.get(serverName));
            log.info(serverName+"关闭成功");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 保持与服务器的连接
     */
    public void keepConnectionAlive(){
        for (String clintName : clientConfigMap.keySet()) {
            Map<String, Object> configMap = clientConfigMap.get(clintName);

            if(configMap != null){
                if(clientMap.containsKey(clintName)){
                    Channel channel = clientMap.get(clintName);
                    if(!channel.isActive()){
                        start0(clintName,(String) configMap.get("ip"),
                                (Integer)configMap.get("port"),(Boolean) configMap.get("sync"));
                    }
                }else {
                    log.info("找到连接信息，尝试重新连接"+ JSONUtil.toJsonStr(configMap));
                    start0(clintName,(String) configMap.get("ip"),
                            (Integer)configMap.get("port"),(Boolean) configMap.get("sync"));
                }
            }
        }
    }


    //下面这两端代码合并下 todo
    private Channel start0(String serverName, String ip, Integer port){

        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new MyInitializerHandler());
            ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
            if(channelFuture.isSuccess()){
                //存入clintMap
                clientMap.put(serverName,channelFuture.channel());
                log.info("连接服务器成功:ip"+ip+":"+port);

                //向jvm 注册一个shutdown的钩子事件，确保netty正常安全退出释放资源
                Thread shutdownThread = new Thread(()->{
                    try {
                        eventLoopGroup.shutdownGracefully();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                shutdownThreadMap.put(serverName,shutdownThread);
                Runtime.getRuntime().addShutdownHook(shutdownThread);

                return channelFuture.channel();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void start1(String serverName, String ip, Integer port){

        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new MyInitializerHandler());
            ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
            if(channelFuture.isSuccess()){
                //存入clintMap
                clientMap.put(serverName,channelFuture.channel());
                log.info("连接服务器成功:ip"+ip+":"+port);
            }
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            eventLoopGroup.shutdownGracefully();
            clientMap.remove(serverName);
            log.info(serverName+"已安全关闭");
        }

    }

}
