package ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb;

import ar.edu.unq.epersgeist.controller.dto.ReporteDTO.SnapshotMongo;
import ar.edu.unq.epersgeist.controller.dto.ReporteDTO.SnapshotNeo;
import ar.edu.unq.epersgeist.controller.dto.ReporteDTO.SnapshotSql;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document("Snapshot") @Getter
public class Snapshot {

    @Id
    private String id;
    private List<SnapshotSql> sql;
    private List<SnapshotMongo> mongo;
    private List<SnapshotNeo> neo4j;
    @CreatedDate
    private LocalDateTime fecha;

    public Snapshot() {
        this.sql = new ArrayList<>();
        this.mongo = new ArrayList<>();
        this.neo4j = new ArrayList<>();
    }

    public void guardarEstadoSql(List<SnapshotSql> sql) {
        sql.forEach(dto -> this.sql.add(dto));
    }

    public void guardarEstadoMongo(List<SnapshotMongo> mongo) {
        mongo.forEach(dto -> this.mongo.add(dto));
    }

    public void guardarEstadoNeo(List<SnapshotNeo> neo4j) {
        neo4j.forEach(dto -> this.neo4j.add(dto));
    }
}
