package com.eleven.nettyserver;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BankService {
    public void transferMoney(String text){
        Map jsonObject = (Map)JSON.parse(text);
        System.out.println(jsonObject.get("data"));
    }
}
