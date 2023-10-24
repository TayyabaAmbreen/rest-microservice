package com.optymyze.codingchallenge.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(Long id){
        super("Resource doesn't exist with id " + id);
    }
}
