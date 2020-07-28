package com.point.clddock.result;


public class DockResult<T> {

    public static final Integer SUCCESS = 1;
    public static final Integer WARN = 2;
    public static final Integer ERROR = 3;

    private int status;// 1:成功，2:警告，3:失败
    private String message;
    private T data;

    public static DockResult<String> success(String message){
        return new DockResult<>(SUCCESS, message);
    }
    public static DockResult fail(String errorMsg){
        return new DockResult(ERROR, errorMsg);
    }

    public DockResult(int status, String message) {
        this.status = status;
        this.message = message;
    }
    public DockResult(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    @Override
    public String toString() {
        return "MsgResult{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
    public boolean ok(){
        return status != ERROR;
    }
    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
