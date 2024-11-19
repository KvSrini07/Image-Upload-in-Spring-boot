package com.example.decode.Response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseBO {
    private int code;
    private String status;
    @JsonProperty("_embedded")
    private Object data;
    private String msg;

    public ResponseBO(int code, String status, Object data, String msg) {
        this.code = code;
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
