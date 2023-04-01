package com.eleven;

import com.eleven.netty.NettyClient;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Order(1)
@Component
@Slf4j
public class BankClientRunner implements ApplicationRunner, ApplicationListener<ContextClosedEvent>, ApplicationContextAware {

    @Value("${nettyServer.ip}")
    private String nettyServerIp;
    @Value("${nettyServer.port}")
    private int nettyServerPort;
    @Value("${nettyServer.name}")
    private String serverName;

    private static boolean nettyRunning = false;

    private ApplicationContext applicationContext;

    private Channel serverChannel;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {

        NettyClient nettyClient = NettyClient.instance();

        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);

        //多客户端修改服务名
        Channel wyRPCServer = nettyClient.start(serverName, nettyServerIp, nettyServerPort);
        if(wyRPCServer != null){
            nettyRunning = true;
        }
        //netty服务通道保活；
        scheduledThreadPoolExecutor.scheduleWithFixedDelay(()->{
            NettyClient.instance().keepConnectionAlive();
        },5,5, TimeUnit.SECONDS);
    }

    public static boolean isNettyRunning(){
        return nettyRunning;
    }
    public void onApplicationEvent(ContextClosedEvent event) {
        if (this.serverChannel != null) {
            this.serverChannel.close();
        }
        log.info("websocket 服务停止");
    }
}
