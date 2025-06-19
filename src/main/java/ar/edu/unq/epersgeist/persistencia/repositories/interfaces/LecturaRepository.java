package ar.edu.unq.epersgeist.persistencia.repositories.interfaces;


import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface LecturaRepository {
    void guardarLectura(Point influxPoint);

    List<FluxRecord> recuperarLecturas();

    void deleteAll();
}