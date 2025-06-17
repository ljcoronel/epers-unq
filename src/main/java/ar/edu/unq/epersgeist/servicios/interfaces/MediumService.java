package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;

import java.util.List;
import java.util.Optional;

public interface MediumService {
    Medium crear(Medium medium);
    Optional<Medium> recuperar(Long mediumId);
    List<Medium> recuperarTodos();
    void actualizar(Medium medium);
    void eliminar(Long idDelMedium);
    void descansar(Long mediumId);
    void exorcisar(Long idMediumExorcista, Long idMediumAExorcisar);
    List<Espiritu> espiritus(Long mediumId);
    Espiritu invocar(Long mediumId, Long espirituId);
    void mover(Long mediumId,Double latitud, Double longitud);
}