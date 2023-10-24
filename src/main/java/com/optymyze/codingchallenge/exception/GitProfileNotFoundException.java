package com.optymyze.codingchallenge.exception;

public class GitProfileNotFoundException extends RuntimeException{

    public GitProfileNotFoundException(Long id){
        super("Git profile url for user with id " + id + " doesn't exist");
    }
}
