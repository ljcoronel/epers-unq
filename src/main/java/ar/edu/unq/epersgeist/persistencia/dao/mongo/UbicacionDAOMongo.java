package ar.edu.unq.epersgeist.persistencia.dao.mongo;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongo.UbicacionMONGODTO;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UbicacionDAOMongo extends MongoRepository<UbicacionMONGODTO, String> {

    @Aggregation(pipeline = {
            "{$match :  {nombre :  '?0'}}"

    })
    public Ubicacion recuperarPorNombre(String nombre);
}
