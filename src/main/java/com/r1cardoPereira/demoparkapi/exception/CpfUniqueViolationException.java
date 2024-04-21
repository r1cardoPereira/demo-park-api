package com.r1cardoPereira.demoparkapi.exception;

public class CpfUniqueViolationException extends RuntimeException {

    public CpfUniqueViolationException(String message){
        super(message);
    }
    
}
