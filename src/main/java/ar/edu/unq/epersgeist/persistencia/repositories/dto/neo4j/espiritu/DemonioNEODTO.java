package ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.espiritu;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import org.springframework.data.neo4j.core.schema.Node;

@Node("DEMONIO")
public class DemonioNEODTO extends EspirituNEODTO{

    public DemonioNEODTO(Espiritu espiritu) {
        super(espiritu);
    }
}

