package ar.edu.unq.epersgeist.persistencia.repositories.interfaces;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UbicacionRepository {
    boolean existeNombre(String nombre, Long id);

    Ubicacion save(Ubicacion ubicacion);

    Optional<Ubicacion> findById(Long id);

    void actualizar(Long id, String nombre, int energia);

    boolean esReferenciado(Long id);

    void deleteById(Long id);

    List<Ubicacion> findAll();


    void guardarLecturaDeTemperatura(String ubicacionId, double temperatura, String timeStamp);

    List<FluxRecord> recuperarLecturas();
}
