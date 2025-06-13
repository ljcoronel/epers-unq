package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.persistencia.dao.mongodb.SnapshotDAOMONGO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.Snapshot;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.SnapshotRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class SnapshotRepositoryImpl implements SnapshotRepository {

    private final SnapshotDAOMONGO  snapshotDAOMONGO;

    public SnapshotRepositoryImpl(SnapshotDAOMONGO snapshotDAOMONGO) {
        this.snapshotDAOMONGO = snapshotDAOMONGO;
    }

    @Override
    public Snapshot crear(Snapshot snapshot) {
        return this.snapshotDAOMONGO.save(snapshot);
    }

    @Override
    public Snapshot recuperarPorFecha(LocalDate fecha) {
        return this.snapshotDAOMONGO.recuperarPorFecha(fecha.atStartOfDay(), fecha.plusDays(1).atStartOfDay().minusNanos(1));
    }
}
