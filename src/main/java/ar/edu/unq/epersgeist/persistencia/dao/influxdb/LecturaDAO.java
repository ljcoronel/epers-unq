package ar.edu.unq.epersgeist.persistencia.dao.influxdb;


import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;

import java.util.List;

public interface LecturaDAO {
    void guardarLectura(Point influxPoint);

    List<FluxRecord> recuperarLecturas();

    public Long recuperarIdDeUbicacionMasPeligrosa();

    public Long recuperarIdDeUbicacionMasConMasTransitoDeEspiritus();

    public List<Long> recuperarIdDeUbicacionesRecuperadas();
}