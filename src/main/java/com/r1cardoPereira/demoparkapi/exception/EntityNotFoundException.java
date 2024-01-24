package com.r1cardoPereira.demoparkapi.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message){
        super(message);
    }
    
}
