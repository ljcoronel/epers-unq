package ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.ubicacion;

import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO.TipoUbicacion;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node(labels = "UBICACION")
public abstract class UbicacionNEODTO{

    @Id
    private Long id;

    private String nombre;

    private int energia;

    public UbicacionNEODTO(Ubicacion ubicacion) {
        id = ubicacion.getId();
        nombre = ubicacion.getNombre();
        energia = ubicacion.getEnergia();
    }

    public static UbicacionNEODTO desdeModelo(Ubicacion ubicacion) {
        TipoUbicacion tipo = TipoUbicacion.obtenerTipoUbicacion(ubicacion);
        return tipo.getDtoNEO(ubicacion);
    }


    public Ubicacion aModelo() {
        return null;
    }
}


