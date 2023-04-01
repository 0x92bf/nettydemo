package com.eleven.netty.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*********************************
 * Created by IntelliJ IDEA.
 * @Author : stz
 * @create 2023/3/27 16:10
 *********************************/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageData {

    private DataCode dataCode;

    private Object data;

    public MessageData(DataCode dataCode){
        this.dataCode = dataCode;
    }

    public enum DataCode {
        SQL, PING, PONG, REQUEST, RESPONSE, OTHER;//REQUEST, RESPONS 临时使用，此处根据业务自行定义后使用
    }
}
