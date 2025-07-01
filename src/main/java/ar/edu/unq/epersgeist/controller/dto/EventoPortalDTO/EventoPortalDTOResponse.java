package ar.edu.unq.epersgeist.controller.dto.EventoPortalDTO;

import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO.UbicacionDTOResponse;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.influx.EventoPortalDTO;

import java.time.Instant;

public record EventoPortalDTOResponse(Long ubicacionId, Instant tiempo, Double intensidad, UbicacionDTOResponse ubicacion) {

    public static EventoPortalDTOResponse desdeModelo(EventoPortalDTO eventoPortal){
        return new EventoPortalDTOResponse(
                eventoPortal.getUbicacionId(),
                eventoPortal.getTiempo(),
                eventoPortal.getIntensidad(),
                UbicacionDTOResponse.desdeModelo(eventoPortal.getUbicacion())
        );
    }
}
