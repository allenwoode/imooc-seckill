package com.example.exception;

/**
 * 秒杀结束运行时异常
 */
public class CloseKillException extends SeckillException {

    public CloseKillException(String message) {
        super(message);
    }

    public CloseKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
