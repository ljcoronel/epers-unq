package ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.Date;

@Node()
public record UbicacionNeo4jDTO(
        @Id
        Long id,

        @Property(name="nombre")
        String nombre,

        int energia,

        Date deleted,

        Date createdAt,

        Date updatedAt) {

    public static UbicacionNeo4jDTO desdeModelo(Ubicacion ubicacion) {
        return new UbicacionNeo4jDTO(ubicacion.getId(), ubicacion.getNombre(), ubicacion.getEnergia(), ubicacion.getDeleted(), ubicacion.getCreatedAt(), ubicacion.getUpdatedAt());
    }

    public Ubicacion aModelo() {
        return null;
    }
}


