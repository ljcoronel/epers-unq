package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.LecturaRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.interfaces.LecturaService;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LecturaServiceImpl implements LecturaService {

    private LecturaRepository lecturaRepository;
    private UbicacionRepository ubicacionRepository;

    public LecturaServiceImpl(LecturaRepository lecturaRepository,UbicacionRepository ubicacionRepository) {
        this.lecturaRepository = lecturaRepository;
        this.ubicacionRepository = ubicacionRepository;
    }

    @Override
    public void recibirLecturaDeTemperatura(Point lectura) {

        this.lecturaRepository.guardarLectura(lectura);
    }

    @Override
    public List<FluxRecord> recuperarLecturas() {
        return this.lecturaRepository.recuperarLecturas();
    }

    @Override
    public Ubicacion ubicacionMasPeligrosa(){
        Long ubicacionId = this.lecturaRepository.recuperarIdDeUbicacionMasPeligrosa();
        return ubicacionRepository.recuperar(ubicacionId).get();
    }

    @Override
    public Ubicacion ubicacionConMasTransitoDeEspiritus(){

        Long ubicacionId = this.lecturaRepository.recuperarIdDeUbicacionMasConMasTransitoDeEspiritus();
        return ubicacionRepository.recuperar(ubicacionId).get();
    }
}