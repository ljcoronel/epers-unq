package ar.edu.unq.epersgeist.persistencia.repositories.interfaces;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.geo.Coordenada;
import org.springframework.data.geo.Point;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.Snapshot;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;
import java.util.Optional;

public interface MediumRepository {
    void actualizar(Medium medium);
    Medium guardar(Medium medium);
    Optional<Medium> recuperar(Long id);
    void eliminar(Long id);

    List<Medium> mediumsEnArea(Coordenada coordenada);

    List<Medium> recuperarTodos();
    List<Medium> mediumsSinEspiritusEn(Long ubicacionId);

    Long mediumConMasDemonios(Long idSantuario);

    boolean estaEntreDistancia(Long id, Point coordenadaReferencia, int minKms, int maxKms);


    void crearSnapshot(Snapshot snapshot);

}
