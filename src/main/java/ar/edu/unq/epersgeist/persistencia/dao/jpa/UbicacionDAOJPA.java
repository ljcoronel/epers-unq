package ar.edu.unq.epersgeist.persistencia.dao.jpa;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.ubicacion.SantuarioJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.ubicacion.UbicacionJPADTO;

import java.util.List;
import java.util.Optional;

public interface UbicacionDAOJPA {
    UbicacionJPADTO crear(UbicacionJPADTO ubicacion);
    Optional<UbicacionJPADTO> recuperar(Long ubicacionId);
    List<UbicacionJPADTO> recuperarTodos();
    void actualizar(Long id, String nombre, int energia);
    void eliminar(Long ubicacionId);
    SantuarioJPADTO santuarioCorrupto();
    boolean ubicacionConNombre(String nombre, Long id);
}
