package ar.edu.unq.epersgeist.persistencia.repositories.interfaces;


import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface LecturaRepository {
    void guardarLectura(Point influxPoint);

    List<FluxRecord> recuperarLecturas();

    public Long recuperarIdDeUbicacionMasPeligrosa();

    public Long recuperarIdDeUbicacionMasConMasTransitoDeEspiritus();

    public List<Long> recuperarIdDeUbicacionesRecuperadas();
}