package com.ch.util;

import java.io.Serializable;

public class Result implements Serializable {

    private static final long serialVersionUID = -3948389268046368059L;

    private String code;

    private String msg;

    private Object data;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    private boolean success;
    public Result() {}

    public Result(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(final String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(final Object data) {
        this.data = data;
    }

    public final class ResponseCode {
        public static final String SUCCESS = "200";   //成功
        public static final String ERROR_999 = "999";// 系统异常
        public static final String ERROR_PARAMS_VALIDATOR="201"; //参数验证错误
        public static final String ERROR_SERVICE_VALIDATOR="300"; //业务验证错误
        public static final String ERROR_DATA_VALIDATOR="400";  //系统数据错误

    }

    public static Result success() {
        Result Result = new Result();
        Result.setCode(ResponseCode.SUCCESS);
        Result.setMsg("成功");
        Result.setData(null);
        return Result;
    }

    public static Result success(Object data) {
        Result Result = new Result();
        Result.setCode(ResponseCode.SUCCESS);
        Result.setMsg("成功");
        Result.setData(data);
        Result.setSuccess(true);
        return Result;
    }

    public static Result failure(String code, String msg) {
        Result Result = new Result();
        Result.setCode(code);
        Result.setMsg(msg);
        Result.setData(null);
        Result.setSuccess(false);
        return Result;
    }

    public static Result failure(String code, String msg, Object data) {
        Result Result = new Result();
        Result.setCode(code);
        Result.setMsg(msg);
        Result.setData(data);
        return Result;
    }

}
