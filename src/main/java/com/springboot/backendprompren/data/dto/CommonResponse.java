package com.springboot.backendprompren.data.dto;

public enum CommonResponse {
    SUCCESS(0,"Success"),Fail(1,"Fail");
    int code;
    String msg;
    CommonResponse(int code, String msg){
        this.msg = msg;
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}