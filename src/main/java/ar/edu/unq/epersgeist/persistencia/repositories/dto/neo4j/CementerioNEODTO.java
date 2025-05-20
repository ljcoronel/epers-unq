package ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j;

import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Node;

@Getter
@Setter

@NoArgsConstructor

@Node("CEMENTERIO")
public class CementerioNEODTO extends UbicacionNEODTO{
    public CementerioNEODTO(Ubicacion ubicacion) {
        super(ubicacion);
    }

    public CementerioNEODTO(Ubicacion ubicacion, UbicacionNEODTO ubicacionNEODTO) {
        super(ubicacion, ubicacionNEODTO);
    }

    @Override
    public Ubicacion aModelo() {
        Cementerio cementerio = new Cementerio(this.getNombre(), this.getEnergia());
        cementerio.setId(this.getId());
        return cementerio;
    }


}
