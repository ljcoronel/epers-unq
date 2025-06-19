package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.modelo.Lectura.Lectura;
import ar.edu.unq.epersgeist.modelo.Lectura.LecturaDeTemperatura;
import com.influxdb.query.FluxRecord;

import java.util.List;

public interface SensorService {
    void guardarLecturaDeTemperatura(LecturaDeTemperatura lectura);
    List<LecturaDeTemperatura> recuperarLecturas();
}
