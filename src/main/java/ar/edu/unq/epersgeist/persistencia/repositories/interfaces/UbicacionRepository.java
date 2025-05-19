package ar.edu.unq.epersgeist.persistencia.repositories.interfaces;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import java.util.List;
import java.util.Optional;

public interface UbicacionRepository {
    void actualizar(Ubicacion ubicacion);
    Ubicacion guardar(Ubicacion ubicacion);
    Optional<Ubicacion> recuperar(Long id);
    void eliminar(Long id);
    List<Ubicacion> recuperarTodos();

    boolean existeNombre(String nombre, Long id);

    boolean esReferenciado(Long id);
}
