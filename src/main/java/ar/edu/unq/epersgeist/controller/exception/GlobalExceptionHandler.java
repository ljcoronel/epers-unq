package ar.edu.unq.epersgeist.controller.exception;

import ar.edu.unq.epersgeist.controller.dto.DTOResponseError;
import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.exception.*;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler({InvalidFieldsException.class , NullRequiredFieldsException.class})
//    public ResponseEntity<Map<String,String>> handleBadRequest(RuntimeException e ){
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
//    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<DTOResponseError> tipoDeParametroInvalido(InvalidFormatException e){

        if (e.getTargetType() == TipoEspiritu.class) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new DTOResponseError(HttpStatus.BAD_REQUEST,
                            "El tipo del espíritu debe ser" + TipoEspiritu.getNombreTipos()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new DTOResponseError(HttpStatus.BAD_REQUEST, e.getMessage()));
    }


//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<DTOResponseMessage> invalidTipoEspiritu(HttpMessageNotReadableException ife) {
//        DTOResponseMessage dtoErrorResponse = new DTOResponseMessage((HttpStatus.BAD_REQUEST), "El tipo del espíritu debe ser ANGEL o DEMONIO.");
//        return new ResponseEntity<>(dtoErrorResponse, dtoErrorResponse.getStatusCode());
//    }

    @ExceptionHandler(ParametroIncorrectoException.class)
    public ResponseEntity<DTOResponseError> badRequestHandler(ParametroIncorrectoException pie) {
        DTOResponseError dtoErrorResponse = new DTOResponseError(HttpStatus.BAD_REQUEST, pie.getMessage());
        return new ResponseEntity<>(dtoErrorResponse, dtoErrorResponse.getStatusError());
    }


    @ExceptionHandler(InvalidPathParameters.class)
    public ResponseEntity<DTOResponseError> invalidPathParameter(InvalidPathParameters ipp) {
        DTOResponseError dtoErrorResponse = new DTOResponseError(ipp.getStatus(), ipp.getMessage());
        return new ResponseEntity<>(dtoErrorResponse, dtoErrorResponse.getStatusError());
    }

    @ExceptionHandler(NombreRepetidoException.class)
    public ResponseEntity<DTOResponseError> nombreRepetido(NombreRepetidoException nre) {
        DTOResponseError dtoErrorResponse = new DTOResponseError(HttpStatus.CONFLICT, nre.getMessage());
        return new ResponseEntity<>(dtoErrorResponse, dtoErrorResponse.getStatusError());
    }

//    @ExceptionHandler(NombreRepetidoException.class)
//    public ResponseEntity<DTOResponseError> invalidPathParameter(NombreRepetidoException rse) {
//        DTOResponseError dtoErrorResponse = new DTOResponseError(HttpStatus.CONFLICT, rse.getMessage());
//        return new ResponseEntity<>(dtoErrorResponse, dtoErrorResponse.getStatusCode());
//    }

//    @ExceptionHandler(ResponseStatusException.class)
//    public ResponseEntity<DTOResponseError> invalidPathParameter(ResponseStatusException  rse) {
//        DTOResponseError dtoErrorResponse = new DTOResponseError(rse.getStatusCode(), rse.getMessage());
//        return new ResponseEntity<>(dtoErrorResponse, dtoErrorResponse.getStatusError());
//    }

    @ExceptionHandler(EntidadInexistenteException.class)
    public ResponseEntity<DTOResponseError> espirituLibre(EntidadInexistenteException eie) {
        DTOResponseError dtoErrorResponse = new DTOResponseError(HttpStatus.BAD_REQUEST, eie.getMessage());
        return new ResponseEntity<>(dtoErrorResponse, dtoErrorResponse.getStatusError());
    }

    @ExceptionHandler(EspirituLibreException.class)
    public ResponseEntity<DTOResponseError> espirituLibre(EspirituLibreException ele) {
        DTOResponseError dtoErrorResponse = new DTOResponseError(HttpStatus.BAD_REQUEST, ele.getMessage());
        return new ResponseEntity<>(dtoErrorResponse, dtoErrorResponse.getStatusError());
    }

    @ExceptionHandler(UbicacionDistintaException.class)
    public ResponseEntity<DTOResponseError> ubicacionDistinta(UbicacionDistintaException ude) {
        DTOResponseError dtoErrorResponse = new DTOResponseError(HttpStatus.BAD_REQUEST, ude.getMessage());
        return new ResponseEntity<>(dtoErrorResponse, dtoErrorResponse.getStatusError());
    }


}

