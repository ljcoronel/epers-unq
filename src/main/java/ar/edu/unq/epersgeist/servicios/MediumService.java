package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;

import java.util.List;

public interface MediumService {
    Medium crear(Medium medium);
    Medium recuperar(Long mediumId);
    List<Medium> recuperarTodos();
    void actualizar(Medium medium);
    void eliminar(Long mediumId);
    void descansar(Long mediumId);
    void exorcisar(Long idMediumExorcista, Long idMediumAExorcisar);
    List<Espiritu> espiritus(Long mediumId);
    Espiritu invocar(Long mediumId, Long espirituId);
}
