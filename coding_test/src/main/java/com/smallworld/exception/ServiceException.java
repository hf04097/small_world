package com.smallworld.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceException extends RuntimeException {
    private Integer code;
    private String message;

    protected ServiceException() {
        super();
    }

    public ServiceException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public ServiceException(String message) {
        super(message);
        this.message = message;
    }

    public ServiceException(Integer code, String message, Exception e) {
        super(message, e);
        this.code = code;
        this.message = message;
    }
}
