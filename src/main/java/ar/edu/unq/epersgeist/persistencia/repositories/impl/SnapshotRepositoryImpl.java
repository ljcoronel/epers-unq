package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.persistencia.dao.mongodb.SnapshotDAOMONGO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.Snapshot;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.SnapshotRepository;
import org.springframework.stereotype.Repository;

@Repository
public class SnapshotRepositoryImpl implements SnapshotRepository {

    private final SnapshotDAOMONGO  snapshotDAOMONGO;

    public SnapshotRepositoryImpl(SnapshotDAOMONGO snapshotDAOMONGO) {
        this.snapshotDAOMONGO = snapshotDAOMONGO;
    }

    public Snapshot crear(Snapshot snapshot) {
        return this.snapshotDAOMONGO.save(snapshot);
    }
}
