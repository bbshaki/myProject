package com.example.myproject.exception;

public class OutOfStockException extends RuntimeException{

    public OutOfStockException(String message){
        super(message);
    }

}
