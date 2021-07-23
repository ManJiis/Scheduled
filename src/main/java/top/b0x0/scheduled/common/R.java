package top.b0x0.scheduled.common;

import lombok.ToString;

import java.io.Serializable;

@ToString
public class R implements Serializable {
    private static final long serialVersionUID = 332858894308937136L;

    private Integer status;
    private String msg;
    private Object data;

    public static R build(Integer status, String msg, Object data) {
        return new R(status, msg, data);
    }

    public static R build(Integer status) {
        return new R(status, null, null);
    }

    public static R ok(Object data) {
        return new R(data);
    }

    public static R ok() {
        return new R(null);
    }


    /**
     * 添加构造方法
     *
     * @param status  /
     * @param message /
     * @return /
     */
    public static R fail(Integer status, String message) {
        return new R(status, message, null);
    }

    public static R fail(String failMsg) {
        return new R(400, failMsg, null);
    }

    public R() {
    }

    public static R build(Integer status, String msg) {
        return new R(status, msg, null);
    }

    private R(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private R(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
