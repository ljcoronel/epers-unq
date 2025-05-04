package ar.edu.unq.epersgeist.controller.exception;

import ar.edu.unq.epersgeist.controller.dto.DTOResponseError;
import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.exception.*;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<DTOResponseError> tipoDeParametroInvalido(InvalidFormatException e){

        if (e.getTargetType() == TipoEspiritu.class) {
            return new ResponseEntity<>(new DTOResponseError("El tipo del espíritu debe ser" + TipoEspiritu.getNombreTipos()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new DTOResponseError(e.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ParametroIncorrectoException.class)
    public ResponseEntity<DTOResponseError> badRequestHandler(ParametroIncorrectoException pie) {
        DTOResponseError dtoErrorResponse = new DTOResponseError( pie.getMessage());
        return new ResponseEntity<>(dtoErrorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(InvalidPathParameters.class)
    public ResponseEntity<DTOResponseError> invalidPathParameter(InvalidPathParameters ipp) {
        DTOResponseError dtoErrorResponse = new DTOResponseError(ipp.getMessage());
        return new ResponseEntity<>(dtoErrorResponse, ipp.getStatus());
    }

    @ExceptionHandler(NombreRepetidoException.class)
    public ResponseEntity<DTOResponseError> nombreRepetido(NombreRepetidoException nre) {
        DTOResponseError dtoErrorResponse = new DTOResponseError(nre.getMessage());
        return new ResponseEntity<>(dtoErrorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntidadInexistenteException.class)
    public ResponseEntity<DTOResponseError> entidadInexistente(EntidadInexistenteException eie) {
        DTOResponseError dtoErrorResponse = new DTOResponseError( eie.getMessage());
        return new ResponseEntity<>(dtoErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EspirituLibreException.class)
    public ResponseEntity<DTOResponseError> espirituLibre(EspirituLibreException ele) {
        DTOResponseError dtoErrorResponse = new DTOResponseError( ele.getMessage());
        return new ResponseEntity<>(dtoErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UbicacionDistintaException.class)
    public ResponseEntity<DTOResponseError> ubicacionDistinta(UbicacionDistintaException ude) {
        DTOResponseError dtoErrorResponse = new DTOResponseError( ude.getMessage());
        return new ResponseEntity<>(dtoErrorResponse, HttpStatus.BAD_REQUEST);
    }


}

