package ar.edu.unq.epersgeist.persistencia.dao.mongo;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongo.EspirituMongoDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EspirituDAOMongo extends MongoRepository<EspirituMongoDTO, Long> {


}
