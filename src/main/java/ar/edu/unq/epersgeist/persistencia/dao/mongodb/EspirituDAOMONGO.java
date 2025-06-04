package ar.edu.unq.epersgeist.persistencia.dao.mongodb;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.EspirituMongoDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EspirituDAOMONGO extends MongoRepository<EspirituMongoDTO, Long> {


}
