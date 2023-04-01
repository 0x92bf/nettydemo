package com.eleven.netty.handler;


import com.eleven.netty.common.MsgConstants;
import com.eleven.netty.entity.SyncMessage;
import com.eleven.commons.EnterprisePM;
import com.eleven.commons.SpringBeanUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/*********************************
 * Created by IntelliJ IDEA.
 * @Author : stz
 * @create 2023/3/27 15:00
 *********************************/
@Slf4j
public class AuthHandler extends ChannelInboundHandlerAdapter {


    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SyncMessage syncMessage = new SyncMessage();
        syncMessage.setType(MsgConstants.MsgType.AUTH_MSG);
        EnterprisePM bean = SpringBeanUtil.getBean(EnterprisePM.class);
        syncMessage.setEnterpriseCode(bean.getEnterpriseCode());
        log.info("鉴权消息发送成功:{}",syncMessage.toString());
        ctx.channel().writeAndFlush(syncMessage);
    }
}
