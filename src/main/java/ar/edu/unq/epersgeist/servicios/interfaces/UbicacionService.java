package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.ClosenessResult;
import ar.edu.unq.epersgeist.persistencia.DegreeResult;

import java.util.List;
import java.util.Optional;

public interface UbicacionService  {
    Ubicacion crear(Ubicacion ubicacion);
    Optional<Ubicacion> recuperar(Long ubicacionId);
    List<Ubicacion> recuperarTodos();
    void actualizar(Ubicacion ubicacion);
    void eliminar(Long ubicacionId);
    List<Espiritu> espiritusEn(Long ubicacionId);
    List<Medium> mediumsSinEspiritusEn(Long ubicacionId);
    void conectar(Long idOrigen, Long idDestino);
    List<Ubicacion> caminoMasCorto(Long idOrigen, Long idDestino);
    List<Ubicacion> ubicacionesSobrecargadas(int umbralDeEnergia);

    Boolean estanConectadas(Long idOrigen, Long idDestino);

    List<ClosenessResult> closenessOf(List<Long> ids);
    List<DegreeResult>incomingDegreeOf(List<Long> ids);
    List<DegreeResult>outgoingDegreeOf(List<Long> ids);

    Ubicacion ubicacionMasPeligrosa();
    Ubicacion ubicacionConMasTransitoDeEspiritus();
    List<Ubicacion> ubicacionesRecuperadas();

}