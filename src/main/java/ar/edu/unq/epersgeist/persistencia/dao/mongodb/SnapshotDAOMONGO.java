package ar.edu.unq.epersgeist.persistencia.dao.mongodb;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.Snapshot;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;

public interface SnapshotDAOMONGO extends MongoRepository<Snapshot, String> {

    @Aggregation(pipeline = {
            "{ $match: { fecha: {$gte: ?0, $lte: ?1 } }}",
            "{ $sort: { fecha: -1 }}",
            "{ $limit: 1}"
    })
    Snapshot recuperarPorFecha(LocalDateTime comienzoDelDia, LocalDateTime finalDelDia);
}
