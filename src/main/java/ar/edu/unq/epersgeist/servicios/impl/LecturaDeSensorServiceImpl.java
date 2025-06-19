package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.persistencia.DAO.influx.LecturaDAOInflux;
import ar.edu.unq.epersgeist.persistencia.DTOs.influx.LecturaDeSensorDTO;
import ar.edu.unq.epersgeist.servicios.interfaces.LecturaDeSensorService;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LecturaDeSensorServiceImpl implements LecturaDeSensorService {

    private LecturaDAOInflux lecturaDAOInflux;

    public LecturaDeSensorServiceImpl(LecturaDAOInflux lecturaDAOInflux) {
        this.lecturaDAOInflux = lecturaDAOInflux;
    }

    @Override
    public void recibirLecturaDeTemperatura(Point lectura) {

        //this.lecturaDAOInflux.guardarLectura(lectura);
    }

    @Override
    public List<FluxRecord> recuperarLecturas() {
        return this.lecturaDAOInflux.recuperarLecturas();
    }
}
