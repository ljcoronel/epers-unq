package ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j;

import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter

@NoArgsConstructor

@Node(labels = "UBICACION")
public abstract class UbicacionNEODTO{

    @Id
    private Long id;

    private String nombre;

    private int energia;

    @Relationship(type = "CONECTADA")
    private List<UbicacionNEODTO> ubicacionesConectadas;

    public UbicacionNEODTO(Ubicacion ubicacion) {
        id = ubicacion.getId();
        nombre = ubicacion.getNombre();
        energia = ubicacion.getEnergia();
        ubicacionesConectadas = ubicacion
                                .getUbicacionesConectadas()
                                .stream()
                                .map(UbicacionNEODTO::desdeModelo).toList(); // TODO chequear recursión
    }

    public static UbicacionNEODTO desdeModelo(Ubicacion ubicacion) {
        if (ubicacion == null) return null;
        return switch (ubicacion){
            case Santuario ignored -> new SantuarioNEODTO(ubicacion);
            case Cementerio ignored -> new CementerioNEODTO(ubicacion);
            default -> throw new IllegalStateException("Unexpected value: " + ubicacion.getClass().getSimpleName());
        };

    }


    public abstract Ubicacion aModelo();

    public Ubicacion aModelo(Ubicacion ubicacion) {
        ubicacion.setUbicacionesConectadas
                (this.ubicacionesConectadas.stream()
                        .map(UbicacionNEODTO::aModelo)
                        .collect(Collectors.toSet()));
        return ubicacion;
    }
}


