package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.modelo.exception.UbicacionConMismoNombreException;
import ar.edu.unq.epersgeist.modelo.exception.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.exception.UbicacionRequestSinNombreException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UbicacionConMismoNombreException.class)
    public Map<String, Object> nombreRepetido(UbicacionConMismoNombreException ex) {
        return Map.of("response_code", HttpStatus.CONFLICT.value(), "description", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UbicacionRequestSinNombreException.class)
    public Map<String, Object> ubicacionSinNombre(UbicacionRequestSinNombreException ex) {
        return Map.of("response_code", HttpStatus.BAD_REQUEST.value(), "description", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UbicacionNoEncontradaException.class)
    public Map<String, Object> ubicacionNoEncontrada(UbicacionNoEncontradaException ex) {
        return Map.of("response_code", HttpStatus.NOT_FOUND.value(), "description", ex.getMessage());
    }
}
