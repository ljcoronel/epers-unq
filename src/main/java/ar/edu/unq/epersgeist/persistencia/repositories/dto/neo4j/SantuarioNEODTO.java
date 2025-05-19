package ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j;


import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import org.springframework.data.neo4j.core.schema.Node;

@Node("SANTUARIO")
public class SantuarioNEODTO extends UbicacionNEODTO{
    public SantuarioNEODTO(Ubicacion ubicacion) {
        super(ubicacion);
    }

}
