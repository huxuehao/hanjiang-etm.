package com.hanjiang.etm.common.exception;

/**
 * 描述：基础异常
 *
 * @author huxuehao
 **/
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private int code;
    private String module;
    private String method;
    private String message;


    public BaseException(int code, String module, String method, String message) {
        this.code = code;
        this.module = module;
        this.method = method;
        this.message = message;
    }

    public BaseException(String module, String method, String message) {
        this.module = module;
        this.method = method;
        this.message = message;
    }

    public BaseException(String message) {
        this.message = message;
    }

    public BaseException() {
    }

    public int getCode() {
        return this.code;
    }

    public String getModule() {
        return this.module;
    }

    public String getMethod() {
        return this.method;
    }

    public String getMessage() {
        return this.message;
    }
}
