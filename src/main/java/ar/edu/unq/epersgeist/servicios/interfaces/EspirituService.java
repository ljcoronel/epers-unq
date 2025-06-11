package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface EspirituService {
    Espiritu crear(Espiritu espiritu);
    Optional<Espiritu> recuperar(Long espirituId);
    List<Espiritu> recuperarTodos();
    void actualizar(Espiritu espiritu);
    void eliminar(Long espirituId);
    List<Demonio> espiritusDemoniacos(Sort.Direction direccion, int pagina, int cantidadPorPagina);
    Medium conectar(Long espirituId, Long mediumId);
    boolean existeConNombre(Espiritu espiritu);
    void dominar(Long espirituDominanteId, Long espirituADominarId);
}
