package ar.edu.unq.epersgeist.persistencia.dao.jpa;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.MediumJPADTO;

import java.util.List;
import java.util.Optional;

public interface MediumDAOJPA {
    MediumJPADTO crear(MediumJPADTO medium);
    Optional<MediumJPADTO> recuperar(Long mediumId);
    List<MediumJPADTO> recuperarTodos();
    void actualizar(String nombre, int manaMax, int mana, Long id);
    void eliminar(Long mediumId);
    List<MediumJPADTO> mediumsSinEspiritusEn(Long ubicacionId);
    MediumJPADTO mediumConMasDemoniosEn(Long santuarioId);
    boolean referenciaAUbicacion(Long ubicacionId);
}
