package ar.edu.unq.epersgeist.persistencia.dao.influxdb;


import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;

import java.util.List;
import java.util.Optional;

public interface LecturaDAO {
    void guardarLectura(Point influxPoint);

    List<FluxRecord> recuperarLecturas();

    Long recuperarIdDeUbicacionMasPeligrosa();

    Optional<Long> recuperarIdDeUbicacionMasConMasTransitoDeEspiritus();

    List<Long> recuperarIdDeUbicacionesRecuperadas();
}