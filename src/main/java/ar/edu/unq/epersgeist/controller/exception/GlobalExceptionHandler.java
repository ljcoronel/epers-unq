package ar.edu.unq.epersgeist.controller.exception;

import ar.edu.unq.epersgeist.controller.dto.DTOResponseError;
import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.exception.*;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFormatException.class)
    public DTOResponseError tipoDeParametroInvalido(InvalidFormatException e){

        if (e.getTargetType() == TipoEspiritu.class) {
            return new DTOResponseError("El tipo del espíritu debe ser" + TipoEspiritu.getNombreTipos());
        }
        return new DTOResponseError(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ParametroIncorrectoException.class)
    public DTOResponseError badRequestHandler(ParametroIncorrectoException pie) {
        return new DTOResponseError(pie.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(NombreRepetidoException.class)
    public DTOResponseError nombreRepetido(NombreRepetidoException nre) {
        return new DTOResponseError(nre.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntidadInexistenteException.class)
    public DTOResponseError entidadInexistente(EntidadInexistenteException eie) {
        return new DTOResponseError(eie.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExorcistaSinAngelesException.class)
    public DTOResponseError ExorcistaSinAngelesException(ExorcistaSinAngelesException e){
            return new DTOResponseError( e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EspirituLibreException.class)
    public DTOResponseError espirituLibre(EspirituLibreException ele) {
        return new DTOResponseError( ele.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UbicacionDistintaException.class)
    public DTOResponseError ubicacionDistinta(UbicacionDistintaException ude) {
        return new DTOResponseError( ude.getMessage());
    }

}
