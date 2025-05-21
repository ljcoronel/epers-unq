package ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.espiritu;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.MediumNEODTO;
import org.springframework.data.neo4j.core.schema.Node;

@Node("DEMONIO")
public class DemonioNEODTO extends EspirituNEODTO{

    public DemonioNEODTO(Espiritu espiritu) {
        super(espiritu);
        setMedium(MediumNEODTO.desdeModelo(espiritu.getMedium(),this));
    }

    public DemonioNEODTO(Espiritu espiritu, MediumNEODTO dtoMediumExistente) {
        super(espiritu);
    }
}

