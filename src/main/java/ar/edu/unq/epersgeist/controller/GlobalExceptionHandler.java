package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.modelo.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(NombreRepetidoException.class)
    public DTOResponseError nombreRepetido(NombreRepetidoException ex) {
        return new DTOResponseError(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RequestBodyIncompletoException.class)
    public DTOResponseError requestBodyIncompleto(RequestBodyIncompletoException ex) {
        return new DTOResponseError(ex.getMessage());
    }
}
