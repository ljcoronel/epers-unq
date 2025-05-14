package ar.edu.unq.epersgeist.persistencia.repositories.interfaces;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.jpa.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.jpa.MediumDAO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
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
