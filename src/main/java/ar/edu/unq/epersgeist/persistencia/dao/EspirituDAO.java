package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;

import java.util.List;
import java.util.Optional;

public interface EspirituDAO {
    void crear(Espiritu espiritu);
    Optional<Espiritu> recuperar(Long idDelEspiritu);
    List<Espiritu> recuperarTodos();
    void eliminar(Espiritu espiritu);
    void actualizar(Espiritu espiritu);
//    List<Espiritu> espiritusDeTipo(String tipo);
}