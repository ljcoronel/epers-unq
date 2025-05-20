package ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j;


import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.espiritu.DemonioNEODTO;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        return new MediumNEODTO(medium);
    }

}
