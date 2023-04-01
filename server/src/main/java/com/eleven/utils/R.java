package com.eleven.utils;

import com.eleven.constant.Constants;

import java.io.Serializable;

import static com.eleven.constant.MessageConstans.SUCCESS_MESSAGE;

/**
 * 响应信息主体
 *
 * @author zhoutianxiang
 * @create 2021-08-04
 */
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 成功
     */
    public static final String SUCCESS = Constants.SUCCESS;

    /**
     * 失败
     */
    public static final String FAIL = Constants.FAIL;

    private String code;

    private Object message;

    private T data;

    public static <T> R<T> ok() {
        return restResult(null, SUCCESS, SUCCESS_MESSAGE);
    }

    public static <T> R<T> ok(String code, T data, String msg) {
        return restResult(data, code, msg);
    }

    public static <T> R<T> fail(String code, T data, String msg) {
        return restResult(data, code, msg);
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, SUCCESS, SUCCESS_MESSAGE);
    }


    public static <T> R<T> ok(T data, String msg) {
        return restResult(data, SUCCESS, msg);
    }

    public static <T> R<T> fail() {
        return restResult(null, FAIL, null);
    }

    public static <T> R<T> fail(String msg) {
        return restResult(null, FAIL, msg);
    }

    public static <T> R<T> fail(T data) {
        return restResult(data, FAIL, null);
    }

    public static <T> R<T> fail(T data, String msg) {
        return restResult(data, FAIL, msg);
    }

    public static <T> R<T> fail(String code, String msg) {
        return restResult(null, code, msg);
    }

    private static <T> R<T> restResult(T data, String code, Object msg) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getMsg() {
        return message;
    }

    public void setMsg(Object msg) {
        this.message = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
