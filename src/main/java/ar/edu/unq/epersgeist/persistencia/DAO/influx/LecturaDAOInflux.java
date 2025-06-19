package ar.edu.unq.epersgeist.persistencia.DAO.influx;

import ar.edu.unq.epersgeist.persistencia.DTOs.influx.LecturaDeSensorDTO;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface LecturaDAOInflux {
    List<FluxRecord> recuperarLecturas();

    void guardarLectura(String ubicacionId, double temperatura, String timeStamp);
}
