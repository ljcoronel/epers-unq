package ar.edu.unq.epersgeist.persistencia.dao.mongodb;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.EspirituMongoDTO;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface EspirituDAOMONGO extends MongoRepository<EspirituMongoDTO, Long> {

    @Aggregation()
    @Query("""
           db.createCollection('shops')
           db.shops.insert($match{id:"$0"})
            db.shops.aggregate([
               { "$geoNear": {
                   "near": {
                       $match{id:"$1"}
                   },
                   "maxDistance": 500 * 1609,
                   "key" : "myLocation",
                   "spherical": true,
                   "distanceField": "distance"
               }}
           ]).pretty()
    """
    )
    boolean estanEntreDistancia(Long espirituDominanteId, Long espirituADominarId, int min, int max);
}
