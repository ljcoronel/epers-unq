package ar.edu.unq.epersgeist.persistencia.repositories.interfaces;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.Snapshot;

public interface SnapshotRepository {
    Snapshot crear(Snapshot snapshot);
}
