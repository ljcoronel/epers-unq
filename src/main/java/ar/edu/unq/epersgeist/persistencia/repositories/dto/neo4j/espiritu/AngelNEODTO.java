package ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.espiritu;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import org.springframework.data.neo4j.core.schema.Node;

@Node("ANGEL")
public class AngelNEODTO extends EspirituNEODTO{

    public AngelNEODTO(Espiritu espiritu) {
        super(espiritu);
    }
}
