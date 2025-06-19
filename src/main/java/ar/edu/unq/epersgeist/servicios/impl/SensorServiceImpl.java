package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Lectura.Lectura;
import ar.edu.unq.epersgeist.modelo.Lectura.LecturaDeTemperatura;
import ar.edu.unq.epersgeist.persistencia.DAO.influx.LecturaDAOInflux;
import ar.edu.unq.epersgeist.servicios.interfaces.SensorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorServiceImpl implements SensorService {

    private LecturaDAOInflux lecturaDAOInflux;

    public SensorServiceImpl(LecturaDAOInflux lecturaDAOInflux) {
        this.lecturaDAOInflux = lecturaDAOInflux;
    }

    @Override
    public void guardarLecturaDeTemperatura(LecturaDeTemperatura lectura) {
        lecturaDAOInflux.guardarLectura(lectura);
    }

    @Override
    public List<LecturaDeTemperatura> recuperarLecturas() {
        return lecturaDAOInflux.recuperarLecturas();
    }
}
