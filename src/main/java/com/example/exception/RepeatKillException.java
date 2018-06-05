package com.example.exception;

import com.example.dto.SeckillExecution;

/**
 * 运行时异常
 * 还有一种是编译期异常
 */
public class RepeatKillException extends SeckillException {

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
