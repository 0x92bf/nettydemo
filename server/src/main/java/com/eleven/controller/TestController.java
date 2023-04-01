package com.eleven.controller;

import cn.hutool.json.JSONUtil;
import com.eleven.netty.common.MsgConstants;
import com.eleven.netty.entity.MessageData;
import com.eleven.netty.entity.SyncMessage;
import com.eleven.nettyserver.ChannelMap;
import com.eleven.utils.R;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Value("${netty.enterpriseCode}")
    private String enterpriseCode;

    @PostMapping("/doPay")
    public R<?> doPay(@RequestParam String name){
        String msgId = "1111111token";
        Channel channel = ChannelMap.getInstance().getChannel(this.enterpriseCode);
        //构造消息
        Map<String,Object> map= new HashMap<>();
        map.put("name",name);
        MessageData messageData = new MessageData();
        messageData.setData(JSONUtil.toJsonStr(map));
        SyncMessage syncMessage = new SyncMessage(msgId,this.enterpriseCode, MsgConstants.MsgType.SERVER_TRANSFER, messageData);
        //log.info(ChannelMap.channelNum+"");
        if(channel == null){
            return R.ok("未获取到中间件链接信息");
        }
        MsgConstants.Transfer.syncMsgCountDownLatchMap.put(msgId,new MsgConstants.Transfer.AsycMsg());

        if(channel.isActive()){
            channel.writeAndFlush(syncMessage);
            MsgConstants.Transfer.AsycMsg asycMsg = MsgConstants.Transfer.syncMsgCountDownLatchMap.get(msgId);
            try {
                asycMsg.getCountDownLatch().await();
            }catch (Exception e){
                return R.ok("接收异常:"+ e.getMessage());
            }
            MessageData re = (MessageData) asycMsg.getResult();
            MsgConstants.Transfer.syncMsgCountDownLatchMap.remove(msgId);
            return R.ok(re.getData().toString());

        }else {
            return R.ok("通道关闭");
        }

    }
}
