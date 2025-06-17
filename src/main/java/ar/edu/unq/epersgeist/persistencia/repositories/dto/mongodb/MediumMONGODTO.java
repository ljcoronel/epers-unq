package ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb;

import ar.edu.unq.epersgeist.modelo.geo.Coordenada;
import ar.edu.unq.epersgeist.modelo.Medium;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Document("Medium")
public class MediumMONGODTO {
    @Id
    private String id;
    @GeoSpatialIndexed(name = "coordenada", type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint coordenada;

    public static MediumMONGODTO desdeModelo(Medium medium){
        MediumMONGODTO dto = new MediumMONGODTO();
        dto.id = medium.getId().toString();
        dto.coordenada = new GeoJsonPoint(medium.getCoordenada());
        return dto;
    }

    public Medium aModelo(Medium medium, UbicacionMONGODTO ubicacionMONGODTO){
        medium.setCoordenada(new Coordenada(getCoordenada().getX(), getCoordenada().getY()));
        ubicacionMONGODTO.aModelo(medium.getUbicacion());
        medium.getAngeles()
                .forEach(angel -> ubicacionMONGODTO.aModelo(angel.getUbicacion()));
        medium.getDemonios()
                .forEach(demonio -> ubicacionMONGODTO.aModelo(demonio.getUbicacion()));
        return medium;
    }
}
