package ar.edu.unq.epersgeist.persistencia.repositories.interfaces;

import ar.edu.unq.epersgeist.modelo.Medium;

import java.util.List;
import java.util.Optional;

public interface MediumRepository {
    void actualizar(Medium medium);
    Medium guardar(Medium medium);
    Optional<Medium> recuperar(Long id);
    void eliminar(Long id);
    List<Medium> recuperarTodos();
    List<Medium> mediumsSinEspiritusEn(Long ubicacionId);

    Long mediumConMasDemonios(Long idSantuario);
}
