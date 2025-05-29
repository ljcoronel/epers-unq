package ar.edu.unq.epersgeist.persistencia.dao.jpa;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.espiritu.DemonioJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.espiritu.EspirituJPADTO;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface EspirituDAOJPA {
    EspirituJPADTO crear(EspirituJPADTO espiritu);
    Optional<EspirituJPADTO> recuperar(Long espirituId);
    List<EspirituJPADTO> recuperarTodos();
    void actualizar(String nombre, int nivelDeConexion, Long id);
    void eliminar(Long espirituId);
    List<EspirituJPADTO> todosLosEspiritusDeMedium(Long mediumId);
    List<DemonioJPADTO> espiritusDemoniacos(Sort.Direction direccion, int pagina, int cantidadPorPagina);
    List<EspirituJPADTO> espiritusEn(Long ubicacionId);
    int cantidadTotalDeDemoniosEn(Long santuarioId);
    int cantidadDeDemoniosLibresEn(Long santuarioId);
    boolean espirituConNombre(String nombre, Long id);
    boolean referenciaAMedium(Long mediumId);
    boolean referenciaAUbicacion(Long ubicacionId);
    void desvincular(Long id);
}
