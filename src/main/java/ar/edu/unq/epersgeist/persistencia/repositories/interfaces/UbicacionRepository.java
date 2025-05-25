package ar.edu.unq.epersgeist.persistencia.repositories.interfaces;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.ClosenessResult;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.ClosenessResultDTO;

import java.util.List;
import java.util.Optional;

public interface UbicacionRepository {
    void actualizar(Ubicacion ubicacion);
    Ubicacion guardar(Ubicacion ubicacion);
    Optional<Ubicacion> recuperar(Long id);
    void eliminar(Long id);
    List<Ubicacion> recuperarTodos();

    boolean existeNombre(String nombre, Long id);

    boolean esReferenciado(Long id);

    boolean estanConectadas(Long idOrigen, Long idDestino);

    List<Ubicacion> caminoMasCorto(Long idOrigen, Long idDestino);

    boolean estanConectadasDeAlgunModo(Long idOrigen, Long idDestino);

    List<Ubicacion> ubicacionesSobrecargadas(int umbralDeEnergia);

    List<ClosenessResultDTO> closenessOf(List<Long> ids);
}
