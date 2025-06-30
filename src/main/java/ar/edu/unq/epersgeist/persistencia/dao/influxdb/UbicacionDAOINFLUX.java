package ar.edu.unq.epersgeist.persistencia.dao.influxdb;


import ar.edu.unq.epersgeist.persistencia.repositories.dto.influx.EventoPortalDTO;

import java.util.List;
import java.util.Optional;

public interface UbicacionDAOINFLUX {
    void cambiarTemperatura(Long id, double temperatura);

    Optional<Long> recuperarIdDeUbicacionMasPeligrosa();

    Optional<Long> recuperarIdDeUbicacionMasConMasTransitoDeEspiritus();

    List<EventoPortalDTO> recuperarEventosDePortales();

    double getTemperatura(Long id);

    boolean tieneDeltaDeTemperaturaMenorOIgual(Long id, int deltaTemp);

}