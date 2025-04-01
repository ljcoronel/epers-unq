package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;

import java.util.List;

public interface MediumDAO {
    void guardar(Medium medium);
    Medium recuperar(Long mediumId);
    List<Medium> recuperarTodos();
    void eliminar(Medium medium);
}
