package ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j;

import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.time.Instant;
import java.util.Date;


@Node(labels = "UBICACION")
public abstract  class UbicacionNEODTO{

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
        if (ubicacion == null) return null;
        return switch (ubicacion){
            case Santuario ignored -> new SantuarioNEODTO(ubicacion);
            case Cementerio ignored -> new CementerioNEODTO(ubicacion);
            default -> throw new IllegalStateException("Unexpected value: " + ubicacion.getClass().getSimpleName());
        };

    }


    public Ubicacion aModelo() {
        return null;
    }
}


