package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.modelo.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({InvalidFieldsException.class , NullRequiredFieldsException.class})
    public String handleBadRequest(RuntimeException e ){
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public String recursoNoEncontradoException(RecursoNoEncontradoException e){
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExorcistaSinAngelesException.class)
    public String ExorcistaSinAngelesException(ExorcistaSinAngelesException e){
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EspirituLibreException.class)
    String EspirituLibreException(EspirituLibreException e){
        return e.getMessage();
    }

}
