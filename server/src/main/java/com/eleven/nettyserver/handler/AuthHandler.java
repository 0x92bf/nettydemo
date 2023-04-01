package com.eleven.nettyserver.handler;

import com.eleven.netty.common.MsgConstants;
import com.eleven.netty.entity.SyncMessage;
import com.eleven.nettyserver.ChannelMap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/*********************************
 * Created by IntelliJ IDEA.
 * @Author : stz
 * @create 2023/3/27 14:51
 *********************************/
@Slf4j
@Component
@ChannelHandler.Sharable
public class AuthHandler extends SimpleChannelInboundHandler<SyncMessage> {

    @Value("${netty.enterpriseCode}")
    private String enterpriseCode;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SyncMessage syncMessage) throws Exception {
            if(syncMessage.getEnterpriseCode().equals(this.enterpriseCode)){
                if(MsgConstants.MsgType.AUTH_MSG == syncMessage.getType()){
                    ChannelMap.getInstance().addChannel(syncMessage.getEnterpriseCode(),ctx.channel());
                    log.info("收到【{}】鉴权消息",syncMessage.getEnterpriseCode());
                    //鉴权成功移除authhandle
                    ctx.channel().pipeline().remove(AuthHandler.class);
                }else {
                    ctx.fireChannelRead(syncMessage);
                }
            }else {
                log.info("非法链接关闭");
                ctx.close();

            }

    }
}
