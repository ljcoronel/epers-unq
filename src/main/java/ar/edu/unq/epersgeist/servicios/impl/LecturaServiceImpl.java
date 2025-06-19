package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.LecturaRepository;
import ar.edu.unq.epersgeist.servicios.interfaces.LecturaService;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LecturaServiceImpl implements LecturaService {

    private LecturaRepository lecturaRepository;

    public LecturaServiceImpl(LecturaRepository lecturaRepository) {
        this.lecturaRepository = lecturaRepository;
    }

    @Override
    public void recibirLecturaDeTemperatura(Point lectura) {

        this.lecturaRepository.guardarLectura(lectura);
    }

    @Override
    public List<FluxRecord> recuperarLecturas() {
        return this.lecturaRepository.recuperarLecturas();
    }
}