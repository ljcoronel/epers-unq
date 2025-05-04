package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.modelo.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({InvalidFieldsException.class , NullRequiredFieldsException.class})
    public Map<String, String> handleBadRequest(RuntimeException e ){
        return Map.of("error", e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public Map<String, String> recursoNoEncontradoException(RecursoNoEncontradoException e){
        return Map.of("error", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExorcistaSinAngelesException.class)
    public Map<String, String> ExorcistaSinAngelesException(ExorcistaSinAngelesException e){
        return Map.of("error", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EspirituLibreException.class)
    public Map<String, String> EspirituLibreException(EspirituLibreException e){
        return Map.of("error", e.getMessage());
    }

}
