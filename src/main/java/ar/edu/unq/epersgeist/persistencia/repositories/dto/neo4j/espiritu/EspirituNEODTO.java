package ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.espiritu;

import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.TipoEspiritu;
import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO.TipoUbicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.UbicacionNEODTO;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node(labels = "ESPIRITU")
public class EspirituNEODTO {

    @Id
    private Long id;

    private String nombre;

    public EspirituNEODTO(Espiritu espiritu) {
        id = espiritu.getId();
        nombre = espiritu.getNombre();
    }

    public static EspirituNEODTO desdeModelo(Espiritu espiritu) {
        TipoEspiritu tipo = TipoEspiritu.getValueOfEspiritu(espiritu);
        return tipo.getDtoNEO(espiritu);
    }


    public Ubicacion aModelo() {
        return null;
    }
}
