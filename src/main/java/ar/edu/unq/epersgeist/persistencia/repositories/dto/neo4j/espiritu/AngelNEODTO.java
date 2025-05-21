package ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.espiritu;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.MediumNEODTO;
import org.springframework.data.neo4j.core.schema.Node;

@Node("ANGEL")
public class AngelNEODTO extends EspirituNEODTO{

    public AngelNEODTO(Espiritu espiritu, MediumNEODTO dtoMediumExistente) {
        super(espiritu);
        setMedium(MediumNEODTO.desdeModelo(espiritu.getMedium(),this));
    }

    public AngelNEODTO(Espiritu espiritu) {
        super(espiritu);
    }
}
