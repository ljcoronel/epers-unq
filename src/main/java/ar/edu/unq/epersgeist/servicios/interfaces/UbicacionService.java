package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;

import java.util.List;
import java.util.Optional;

public interface UbicacionService  {
    Ubicacion crear(Ubicacion ubicacion);
    Optional<Ubicacion> recuperar(Long ubicacionId);
    List<Ubicacion> recuperarTodos();
    void actualizar(Ubicacion ubicacion);
    void eliminar(Long ubicacionId);
    List<Espiritu> espiritusEn(Long ubicacionId);
    List<Medium> mediumsSinEspiritusEn(Long ubicacionId);
}