package ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import org.springframework.data.neo4j.core.schema.Node;

@Node("CEMENTERIO")
public class CementerioNEODTO extends UbicacionNEODTO{

    public CementerioNEODTO(Ubicacion ubicacion) {
        super(ubicacion);
    }


}
