package ar.edu.unq.epersgeist.persistencia.repositories.interfaces;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.Snapshot;

import java.time.LocalDate;

public interface SnapshotRepository {
    Snapshot crear(Snapshot snapshot);
    Snapshot recuperarPorFecha(LocalDate fecha);
}
