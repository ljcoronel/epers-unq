package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Medium;

import java.util.List;
import java.util.Optional;

public interface MediumDAO {
    Medium crear(Medium medium);
    Optional<Medium> recuperar(Long mediumId);
    List<Medium> recuperarTodos();
    void actualizar(Medium medium);
    void eliminar(Long idDelMedium);

}
