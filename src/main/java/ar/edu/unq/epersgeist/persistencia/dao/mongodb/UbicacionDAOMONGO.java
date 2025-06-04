package ar.edu.unq.epersgeist.persistencia.dao.mongodb;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.UbicacionMONGODTO;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UbicacionDAOMONGO extends MongoRepository<UbicacionMONGODTO, Long> {

    @Query("{ 'area': { $geoIntersects: { $geometry: ?0 } } }")
    Optional<UbicacionMONGODTO> findByPuntoDentroDelTerritorio(GeoJsonPoint point);
}
