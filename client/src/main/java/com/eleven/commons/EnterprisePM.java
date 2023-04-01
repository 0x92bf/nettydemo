package com.eleven.commons;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/*********************************
 * Created by IntelliJ IDEA.
 * @Author : stz
 * @create 2023/3/27 15:12
 *********************************/
@Component
@Data
public class EnterprisePM {

    @Value("${enterpriseCode}")
    private String enterpriseCode;
}
