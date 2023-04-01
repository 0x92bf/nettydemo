package com.eleven.netty.entity;

import lombok.Data;

/********************************************************************
 * 登录返回信息.
 * @version 0.1
 * @date 2023/3/9 14:04
 * @author stz
 ********************************************************************/
@Data
public class LoginInfo {

    private boolean success;

    private String errorCode;

    private String errorMsg;

    private String serverIp;

    private Integer serverPort;

    private String token;

    public static class LoginErrorCode {
        /** 鉴权成功 */
        public final static String AUTH_SUCCESS = "200";

        /** 黑名单IP */
        public final static String IP_BLACK = "101";
        /** 黑名单企业 */
        public final static String ENTERPRISE_BLACK = "102";
        /** ip校验失败 */
        public final static String IP_CHECK_ERROR = "103";
        /** 密钥校验失败 */
        public final static String SECRET_ERROR = "104";



        /** 密钥校验失败 */
        public final static String NOT_FOUNT_SERVER = "104";
    }


    public static LoginInfo error(String errorCode,String errorMsg){
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setSuccess(true);
        loginInfo.setErrorCode(errorCode);
        loginInfo.setErrorMsg(errorMsg);
        return loginInfo;
    }

    public static LoginInfo success(){
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setSuccess(true);
        loginInfo.setErrorCode(LoginErrorCode.AUTH_SUCCESS);
        loginInfo.setErrorMsg("鉴权成功");
        return loginInfo;
    }
}
