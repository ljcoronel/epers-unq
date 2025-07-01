package ar.edu.unq.epersgeist.controller.dto.ReporteDTO.Snapshot;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.Snapshot;

import java.time.format.DateTimeFormatter;
import java.util.List;

public record SnapshotResponseDTO(List<SnapshotSql> sql, List<SnapshotMongo> mongo, List<SnapshotNeo> neo4j, String fecha) {

    public static SnapshotResponseDTO desdeModelo(Snapshot snapshot) {
        return new SnapshotResponseDTO(
                snapshot.getSql(),
                snapshot.getMongo(),
                snapshot.getNeo4j(),
                snapshot.getFecha().format(DateTimeFormatter.ISO_DATE)
        );
    }
}
