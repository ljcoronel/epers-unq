package ar.edu.unq.epersgeist.persistencia.dao.mongodb;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.Snapshot;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SnapshotDAOMONGO extends MongoRepository<Snapshot, String> {
}
