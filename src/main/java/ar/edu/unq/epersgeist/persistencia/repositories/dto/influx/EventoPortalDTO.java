package ar.edu.unq.epersgeist.persistencia.repositories.dto.influx;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class EventoPortalDTO {

    private Long ubicacionId;
    private Instant tiempo;
    private Double intensidad;
    private Ubicacion ubicacion;

    public EventoPortalDTO(Long ubicacionId, Instant tiempo, Double intensidad) {
        this.ubicacionId = ubicacionId;
        this.tiempo = tiempo;
        this.intensidad = intensidad;
    }

    public EventoPortalDTO(Ubicacion ubicacion, Instant tiempo, Double intensidad) {
        this.ubicacion = ubicacion;
        this.tiempo = tiempo;
        this.intensidad = intensidad;
        this.ubicacionId = ubicacion.getId();

    }

}