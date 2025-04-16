package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import org.springframework.stereotype.Repository;
import org.hibernate.query.SortDirection;

import java.util.List;
import java.util.Optional;

@Repository
public interface EspirituDAO {
    Espiritu crear(Espiritu espiritu);
    Optional<Espiritu> recuperar(Long idDelEspiritu);
    List<Espiritu> recuperarTodos();
    void eliminar(Long idDelEspiritu);
    void actualizar(Espiritu espiritu);
    List<Espiritu> todosLosEspiritusDeMedium(Long mediumId);
    <T extends Espiritu> List<T> espiritusDeTipo(SortDirection direccion, int pagina, int cantidadPorPagina, Class<T> clase);
}