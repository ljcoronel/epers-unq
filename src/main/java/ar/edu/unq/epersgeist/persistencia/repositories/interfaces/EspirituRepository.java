package ar.edu.unq.epersgeist.persistencia.repositories.interfaces;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;

import java.util.List;
import java.util.Optional;

public interface EspirituRepository {
    void actualizar(Espiritu espiritu);
    Espiritu guardar(Espiritu espiritu);
    Optional<Espiritu> recuperar(Long id);
    void eliminar(Long id);
    List<Espiritu> recuperarTodos();
}
