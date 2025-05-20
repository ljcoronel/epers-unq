package ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j;


import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Node;

@Getter
@Setter

@NoArgsConstructor

@Node("SANTUARIO")
public class SantuarioNEODTO extends UbicacionNEODTO{
    public SantuarioNEODTO(Ubicacion ubicacion) {
        super(ubicacion);
    }

    public SantuarioNEODTO(Ubicacion ubicacion, UbicacionNEODTO ubicacionNEODTO) {
        super(ubicacion, ubicacionNEODTO);
    }

    @Override
    public Ubicacion aModelo() {
        Santuario santuario = new Santuario(this.getNombre(), this.getEnergia());
        santuario.setId(this.getId());
        return santuario;
    }

}
