package ar.edu.unq.epersgeist.persistencia.dao.mongodb;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.EspirituMONGODTO;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.Optional;


@Repository
public interface EspirituDAOMONGO extends MongoRepository<EspirituMONGODTO, Long> {

    @Query("""
           {_id:?0, coordenada: {
                       $nearSphere: {
                         $geometry: ?1,
                         $minDistance: ?2,
                         $maxDistance: ?3
                       }
                     }
                   }
    """
    )
    Optional<EspirituMONGODTO> estanEntreDistancia(Long espirituDominanteId, GeoJsonPoint coordenadasDelEspirituDominado, int minKms, int maxKms);
}
