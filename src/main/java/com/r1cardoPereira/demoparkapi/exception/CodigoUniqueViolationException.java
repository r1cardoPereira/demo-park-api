package com.r1cardoPereira.demoparkapi.exception;

public class CodigoUniqueViolationException extends RuntimeException {

    public CodigoUniqueViolationException(String message){
        super(message);
    }
}
