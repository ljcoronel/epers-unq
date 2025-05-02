package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.modelo.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(NombreRepetidoException.class)
    public Map<String, Object> nombreRepetido(NombreRepetidoException ex) {
        return Map.of("response_code", HttpStatus.CONFLICT.value(), "description", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RequestBodyIncompletoException.class)
    public Map<String, Object> requestBodyIncompleto(RequestBodyIncompletoException ex) {
        return Map.of("response_code", HttpStatus.BAD_REQUEST.value(), "description", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public Map<String, Object> recursoNoEncontrado(RecursoNoEncontradoException ex) {
        return Map.of("response_code", HttpStatus.NOT_FOUND.value(), "description", ex.getMessage());
    }
}
