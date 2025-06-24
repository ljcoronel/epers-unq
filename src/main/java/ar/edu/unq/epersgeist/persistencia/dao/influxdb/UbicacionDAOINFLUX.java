package ar.edu.unq.epersgeist.persistencia.dao.influxdb;


import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;

import java.util.List;
import java.util.Optional;

public interface UbicacionDAOINFLUX {
    void guardarDatos(Point influxPoint);

    Long recuperarIdDeUbicacionMasPeligrosa();

    Optional<Long> recuperarIdDeUbicacionMasConMasTransitoDeEspiritus();

    List<Long> recuperarIdDeUbicacionesRecuperadas();

    double getTemperatura(Long id);

    boolean tieneDeltaDeTemperaturaMenorOIgual(Long id, int deltaTemp);
}