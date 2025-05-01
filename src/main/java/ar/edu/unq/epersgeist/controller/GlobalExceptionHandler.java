package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.TipoEspiritu;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLException;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler({InvalidFieldsException.class , NullRequiredFieldsException.class})
//    public ResponseEntity<Map<String,String>> handleBadRequest(RuntimeException e ){
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
//    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Map<String,String>> badParameter(InvalidFormatException e){

        if (e.getTargetType().getSimpleName().equals(TipoEspiritu.class.getSimpleName())) { // TODO esto estaría bien?
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "El tipo del espíritu debe ser ANGEL o DEMONIO."));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
    }

}

