package ar.edu.unq.epersgeist.persistencia.repositories.interfaces;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.ClosenessResult;
import ar.edu.unq.epersgeist.persistencia.DegreeResult;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.Snapshot;
import org.springframework.data.geo.Point;

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
    void conectar(Long idOrigen, Long idDestino);

    List<Ubicacion> caminoMasCorto(Long idOrigen, Long idDestino);
    boolean estanConectadasDeAlgunModo(Long idOrigen, Long idDestino);

    List<Ubicacion> ubicacionesSobrecargadas(int umbralDeEnergia);

    List<ClosenessResult> closenessOf(List<Long> ids);
    List<DegreeResult> outgoingDegreeOf(List<Long> ids);
    List<DegreeResult> incomingDegreeOf(List<Long> ids);

    Optional<Ubicacion> encontrarUbicacionQueContiene(Point punto);

    boolean existeUbicacionEnArea(List<Point> territorio);
    void crearSnapshot(Snapshot snapshot);

    Optional<Ubicacion> ubicacionMasFria();
    Optional<Ubicacion> ubicacionConMayorVariacionDeTemperaturas();
    List<Ubicacion> ubicacionesFriasQueVolvieronACalentarse();

    void cambiarTemperatura(Long id, double temperatura);

    double getTemperatura(Long id);

    boolean tieneDeltaDeTemperaturaMenorOIgual(Long id, double deltaTemp);
}
