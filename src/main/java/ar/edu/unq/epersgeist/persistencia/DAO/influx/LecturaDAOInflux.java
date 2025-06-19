package ar.edu.unq.epersgeist.persistencia.DAO.influx;

import ar.edu.unq.epersgeist.modelo.Lectura.Lectura;
import ar.edu.unq.epersgeist.modelo.Lectura.LecturaDeTemperatura;
import com.influxdb.query.FluxRecord;

import java.util.List;

public interface LecturaDAOInflux {
    List<LecturaDeTemperatura> recuperarLecturas();

    void guardarLectura(LecturaDeTemperatura lectura);
}
