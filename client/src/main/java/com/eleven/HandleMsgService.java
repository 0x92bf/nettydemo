package com.eleven;

import cn.hutool.json.JSONUtil;
import com.eleven.netty.entity.SyncMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HandleMsgService {

    public static SyncMessage doHandle(SyncMessage syncMessage){
        String str = (String) syncMessage.getBody().getData();
        log.info("接收到订单数据:"+str);
        //调用银行服务
        Map<String,Object> map = new HashMap<>();

        try {
            map.put("data","银行处理中");
            Thread.sleep(10000);
        }catch (Exception e){

        }

        syncMessage.getBody().setData(JSONUtil.toJsonStr(map));
        return syncMessage;
    }
}
