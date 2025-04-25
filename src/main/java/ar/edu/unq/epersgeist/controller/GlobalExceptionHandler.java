package ar.edu.unq.epersgeist.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<Error> handle(IllegalAccessException ex){
        String message = "eerror";
        Error error = new Error("message");
        return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
    }

}

