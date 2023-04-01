package com.eleven.nettyserver;

import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ChannelMap {


    private ChannelMap(){}


    private static class SingletonHolder{
        private final static ChannelMap CHANNEL_MANAGER = new ChannelMap();
    }

    public static ChannelMap getInstance(){
        return SingletonHolder.CHANNEL_MANAGER;
    }


    /**
     * 系统所有在线用户，可能用不上..先注释..
     */
    //private ChannelGroup allChannelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 存储在线企业id和channelId的映射，企业ID为enterpriseCode
     */
    //private Map<String, ChannelId> userChannelMap = new ConcurrentHashMap<String, ChannelId>();
    private Map<String, Channel> userChannelMap = new ConcurrentHashMap<String, Channel>();


    /**
     * 在线channal的心跳计数器
     */
    private Map<String, AtomicInteger> heartMap = new ConcurrentHashMap<>();

    public Map<String, AtomicInteger> getHeartMap() {
        return heartMap;
    }



    /**
     * 根据用户id获取channel
     * @param userId
     * @return
     */
    public Channel getChannel(String userId){
        /*ChannelId channelId = userChannelMap.get(userId);
        if(null == channelId){
            return null;
        }
        return allChannelGroup.find(channelId);*/

        return userChannelMap.get(userId);
    }

    /**
     * 添加在线channal
     * @param userId
     * @param channel
     */
    public void addChannel(String userId, Channel channel){
        /*allChannelGroup.add(channel);
        userChannelMap.put(userId,channel.id());*/

        userChannelMap.put(userId,channel);
    }

    /**
     * 根据用户id移除channel
     * @param userId
     */
    public void removeChannel(String userId){
       /* ChannelId channelId = userChannelMap.get(userId);
        allChannelGroup.remove(channelId);*/

        userChannelMap.remove(userId);
    }
}
