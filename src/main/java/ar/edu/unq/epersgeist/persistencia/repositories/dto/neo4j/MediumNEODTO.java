package ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.ubicacion.UbicacionNEODTO;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node(labels = "MEDIUM")
public class MediumNEODTO {
    @Id
    private Long id;

    @Relationship(type = "TIENE_UBICACION")
    private UbicacionNEODTO ubicacion;

    public MediumNEODTO(Medium medium) {
        this.id = medium.getId();
        this.ubicacion = UbicacionNEODTO.desdeModelo(medium.getUbicacion());
    }

    public static MediumNEODTO desdeModelo(Medium medium) {
        if(medium == null) return null;
        return new MediumNEODTO(medium);
    }

}
