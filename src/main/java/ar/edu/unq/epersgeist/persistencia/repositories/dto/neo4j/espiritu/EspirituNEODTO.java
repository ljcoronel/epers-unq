package ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.espiritu;

import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.ubicacion.UbicacionNEODTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Node(labels = "ESPIRITU")
public abstract class EspirituNEODTO {

    @Id
    private Long id;

    private String nombre;

    @Relationship(type = "TIENE_UBICACION")
    private UbicacionNEODTO ubicacion;

    public EspirituNEODTO(Espiritu espiritu) {
        this.id = espiritu.getId();
        this.nombre = espiritu.getNombre();
        this.ubicacion = UbicacionNEODTO.desdeModelo(espiritu.getUbicacion());
    }

    public static EspirituNEODTO desdeModelo(Espiritu espiritu) {
        TipoEspiritu tipo = TipoEspiritu.getValueOfEspiritu(espiritu);
        return tipo.getDtoNEO(espiritu);
    }
}
