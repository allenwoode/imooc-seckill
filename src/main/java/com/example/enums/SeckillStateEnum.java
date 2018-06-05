package com.example.enums;

public enum SeckillStateEnum {
    SUCCESS(1, "秒杀成功"),
    CLOSE(0, "秒杀结束"),
    REPEAT(-1, "重复秒杀"),
    REWRITE(-2, "MD5错误"),
    INNER_ERROR(-3, "系统异常");

    private int state;
    private String stateInfo;

    SeckillStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public static SeckillStateEnum stateOf(int index) {
        for (SeckillStateEnum state : values()) {
            if (state.getState() == index)
                return state;
        }
        return null;
    }
}
