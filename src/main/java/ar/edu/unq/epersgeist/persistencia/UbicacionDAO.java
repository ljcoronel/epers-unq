package ar.edu.unq.epersgeist.persistencia;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UbicacionDAO {
    Ubicacion crear(Ubicacion ubicacion);
    Optional<Ubicacion> recuperar(Long ubicacionId);
    List<Ubicacion> recuperarTodos();
    void actualizar(Ubicacion ubicacion);
    void eliminar(Long ubicacionId);
}
