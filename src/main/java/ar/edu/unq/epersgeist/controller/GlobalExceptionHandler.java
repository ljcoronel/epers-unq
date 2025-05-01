package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.DtoResponseError;
import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.TipoEspiritu;
import ar.edu.unq.epersgeist.controller.exception.invalidPathParameters;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler({InvalidFieldsException.class , NullRequiredFieldsException.class})
//    public ResponseEntity<Map<String,String>> handleBadRequest(RuntimeException e ){
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
//    }

//    @ExceptionHandler(InvalidFormatException.class)
//    public ResponseEntity<Map<String,String>> badParameter(InvalidFormatException e){
//
//        if (e.getTargetType().getSimpleName().equals(TipoEspiritu.class.getSimpleName())) { // TODO esto estaría bien?
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "El tipo del espíritu debe ser ANGEL o DEMONIO."));
//        }
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
//    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<DtoResponseError> invalidTipoEspiritu(HttpMessageNotReadableException ife) {
        DtoResponseError dtoErrorResponse = new DtoResponseError((HttpStatus.BAD_REQUEST), "El tipo del espíritu debe ser ANGEL o DEMONIO.");
        return new ResponseEntity<>(dtoErrorResponse, dtoErrorResponse.getStatusCode());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<DtoResponseError> badRequestHandler(IllegalArgumentException iea) {
        DtoResponseError dtoErrorResponse = new DtoResponseError((HttpStatus.BAD_REQUEST), iea.getMessage());
        return new ResponseEntity<>(dtoErrorResponse, dtoErrorResponse.getStatusCode());
    }


    @ExceptionHandler(invalidPathParameters.class)
    public ResponseEntity<DtoResponseError> invalidPathParameter(invalidPathParameters  ipp) {
        DtoResponseError dtoErrorResponse = new DtoResponseError(ipp.getStatus(), ipp.getMessage());
        return new ResponseEntity<>(dtoErrorResponse, dtoErrorResponse.getStatusCode());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<DtoResponseError> invalidPathParameter(ResponseStatusException  rse) {
        DtoResponseError dtoErrorResponse = new DtoResponseError(rse.getStatusCode(), rse.getMessage());
        return new ResponseEntity<>(dtoErrorResponse, dtoErrorResponse.getStatusCode());
    }


}

