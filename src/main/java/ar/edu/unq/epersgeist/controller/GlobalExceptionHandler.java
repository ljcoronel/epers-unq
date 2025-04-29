package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.modelo.exception.InvalidFieldsException;
import ar.edu.unq.epersgeist.modelo.exception.NullRequiredFieldsException;
import ar.edu.unq.epersgeist.modelo.exception.RecursoNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLException;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({InvalidFieldsException.class , NullRequiredFieldsException.class})
    public ResponseEntity<Map<String,String>> handleBadRequest(RuntimeException e ){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String,String>> recursoNoEncontradoException(RecursoNoEncontradoException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    }

}

