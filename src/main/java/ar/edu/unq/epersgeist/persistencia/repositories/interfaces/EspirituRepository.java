package ar.edu.unq.epersgeist.persistencia.repositories.interfaces;

import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface EspirituRepository {
    void actualizar(Espiritu espiritu);
    Espiritu guardar(Espiritu espiritu);
    Optional<Espiritu> recuperar(Long id);
    void eliminar(Long id);
    List<Espiritu> recuperarTodos();

    boolean espirituConNombre(String nombre, Long id);

    List<Demonio> espiritusDemoniacos(Sort.Direction direccion, int pagina, int cantidadPorPagina);

    List<Espiritu> todosLosEspiritusDeMedium(Long id);

    List<Espiritu> espiritusEn(Long ubicacionId);

    Long santuarioCorrupto();

    int cantidadTotalDeDemonios(Long idSantuario);

    int cantidadDeDemoniosLibresEn(Long idSantuario);

    void desvincular(Long id);
}
