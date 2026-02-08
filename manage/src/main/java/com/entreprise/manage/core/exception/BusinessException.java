package com.entreprise.manage.core.exception;

// src/main/java/com/entreprise/manage/core/exception/BusinessException.java
// package com.entreprise.manage.core.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}