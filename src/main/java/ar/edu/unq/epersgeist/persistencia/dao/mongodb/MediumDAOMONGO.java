package ar.edu.unq.epersgeist.persistencia.dao.mongodb;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.MediumMONGODTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MediumDAOMONGO extends MongoRepository<MediumMONGODTO, String> {
}
