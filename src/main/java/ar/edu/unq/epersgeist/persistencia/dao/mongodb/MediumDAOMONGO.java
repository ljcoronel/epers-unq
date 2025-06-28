package ar.edu.unq.epersgeist.persistencia.dao.mongodb;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.MediumMONGODTO;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MediumDAOMONGO extends MongoRepository<MediumMONGODTO, String> {

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
    Optional<MediumMONGODTO> estanEntreDistancia(Long id, GeoJsonPoint coordenada, int minKms, int maxKms);

    @Query("""
        {coordenada: {
            $nearSphere: {
                $geometry: ?0,
                $minDistance: ?2,
                $maxDistance: ?3
            }
        }
        }
    """)
    List<MediumMONGODTO> mediumsEnArea(GeoJsonPoint punto, int minkm, int maxkm);
}
