package ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j;

import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO.TipoUbicacion;
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
import java.util.Objects;
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
                                .map(dto -> desdeModelo(dto)).toList();
    }

    public UbicacionNEODTO(Ubicacion ubicacion, UbicacionNEODTO dtoUbicacionYaCreada) {
        id = ubicacion.getId();
        nombre = ubicacion.getNombre();
        energia = ubicacion.getEnergia();
        ubicacionesConectadas = ubicacion
                .getUbicacionesConectadas()
                .stream()
                .map(dto -> mappearDtoConYaCreada(dtoUbicacionYaCreada, dto)).toList();
    }

    private static UbicacionNEODTO mappearDtoConYaCreada(UbicacionNEODTO dtoUbicacionYaCreada, Ubicacion dto) { // para evitar recursión
        if (Objects.equals(dto.getId(), dtoUbicacionYaCreada.getId())) return dtoUbicacionYaCreada;
        return desdeModelo(dto);
    }

    public static UbicacionNEODTO desdeModelo(Ubicacion ubicacion) {
        TipoUbicacion tipo = TipoUbicacion.obtenerTipoUbicacion(ubicacion);
        return tipo.getDtoNEO(ubicacion);
    }

    public static UbicacionNEODTO desdeModelo(Ubicacion ubicacion, UbicacionNEODTO dtoUbicacionYaCreada) {
        TipoUbicacion tipo = TipoUbicacion.obtenerTipoUbicacion(ubicacion);
        return tipo.getDtoNEO(ubicacion, dtoUbicacionYaCreada);
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


