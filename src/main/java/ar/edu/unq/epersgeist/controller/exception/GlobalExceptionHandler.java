package ar.edu.unq.epersgeist.controller.exception;

import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.TipoEspiritu;
import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO.TipoUbicacion;
import ar.edu.unq.epersgeist.modelo.exception.*;
import ar.edu.unq.epersgeist.modelo.exception.espiritu.EspirituDominadoException;
import ar.edu.unq.epersgeist.modelo.exception.espiritu.EspirituLibreException;
import ar.edu.unq.epersgeist.modelo.exception.espiritu.EspirituMuyLejanoException;
import ar.edu.unq.epersgeist.modelo.exception.medium.ExorcistaSinAngelesException;
import ar.edu.unq.epersgeist.modelo.exception.medium.MediumEnDistintaUbicacionException;
import ar.edu.unq.epersgeist.modelo.exception.ubicacion.UbicacionDistintaException;
import ar.edu.unq.epersgeist.modelo.exception.ubicacion.UbicacionesNoConectadasException;
import ar.edu.unq.epersgeist.servicios.exception.AreaOcupadaException;
import ar.edu.unq.epersgeist.servicios.exception.TemperaturaMuyAltaException;
import ar.edu.unq.epersgeist.servicios.exception.UbicacionLejanaException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFormatException.class)
    public DTOResponseError tipoDeParametroInvalido(InvalidFormatException e) {

        if (e.getTargetType() == TipoEspiritu.class) {
            return new DTOResponseError("El tipo del espíritu debe ser" + TipoEspiritu.getNombreTipos());
        }
        if (e.getTargetType() == TipoUbicacion.class) {
            return new DTOResponseError("El tipo de la ubicación debe ser" + TipoUbicacion.getNombreTipos());
        }
        return new DTOResponseError("El formato es inválido.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ParametroIncorrectoException.class)
    public DTOResponseError badParameterHandler(ParametroIncorrectoException pie) {
        return new DTOResponseError(pie.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MediumEnDistintaUbicacionException.class)
    public DTOResponseError MediumsEnDistintaUbicacion(MediumEnDistintaUbicacionException pie) {
        return new DTOResponseError(pie.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntidadTodaviaReferenciadaException.class)
    public DTOResponseError todaviaReferenciada(EntidadTodaviaReferenciadaException tre) {
        return new DTOResponseError(tre.getMessage());
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
    public DTOResponseError ExorcistaSinAngelesException(ExorcistaSinAngelesException e) {
        return new DTOResponseError(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EspirituLibreException.class)
    public DTOResponseError espirituLibre(EspirituLibreException ele) {
        return new DTOResponseError(ele.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UbicacionDistintaException.class)
    public DTOResponseError ubicacionDistinta(UbicacionDistintaException ude) {
        return new DTOResponseError(ude.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UbicacionesNoConectadasException.class)
    public DTOResponseError ubicacionesNoConectadas(UbicacionesNoConectadasException ude) {
        return new DTOResponseError(ude.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UbicacionLejanaException.class)
    public DTOResponseError ubicacionLejana(UbicacionLejanaException ule) {
        return new DTOResponseError(ule.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EspirituMuyLejanoException.class)
    public DTOResponseError espirituMuyLejano(EspirituMuyLejanoException emle) {
        return new DTOResponseError( emle.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EspirituDominadoException.class)
    public DTOResponseError espirituDominado(EspirituDominadoException ede) {
        return new DTOResponseError( ede.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AreaOcupadaException.class)
    public DTOResponseError areaOcupada(AreaOcupadaException are) {
        return new DTOResponseError(are.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PoligonoNoValidoException.class)
    public  DTOResponseError poligonoNoValido(PoligonoNoValidoException p) {
        return new DTOResponseError(p.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FueraDelMapaException.class)
    public DTOResponseError fueraDelMapa(FueraDelMapaException f) {
        return new DTOResponseError(f.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TemperaturaMuyAltaException.class)
    public DTOResponseError tempMuyAlta(TemperaturaMuyAltaException tmae) {return new DTOResponseError(tmae.getMessage());}
}
