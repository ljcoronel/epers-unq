package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.modelo.exception.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.exception.UbicacionRequestSinNombreException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String nombreRepetido() {
        return "No se puede crear una ubicación con nombre repetido";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UbicacionRequestSinNombreException.class)
    public String ubicacionSinNombre(UbicacionRequestSinNombreException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UbicacionNoEncontradaException.class)
    public String ubicacionNoEncontrada(UbicacionNoEncontradaException ex) {
        return ex.getMessage();
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
//    public String parametroInvalido() {
//        return "El ID es inválido";
//    }
}
