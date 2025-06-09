package ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb;

import ar.edu.unq.epersgeist.modelo.Coordenada;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document("Espiritu")
public class EspirituMONGODTO {

    @Id
    private Long id;
    @GeoSpatialIndexed(name = "coordenada", type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint coordenada;
    private Long idUbicacion;

    public EspirituMONGODTO(Espiritu espiritu) {
        this.id = espiritu.getId();
        this.coordenada = new GeoJsonPoint(espiritu.getCoordenada());
        this.idUbicacion = espiritu.getUbicacion().getId();
    }

    public static EspirituMONGODTO desdeModelo(Espiritu espiritu){
        return new EspirituMONGODTO(espiritu);
    }

    public Espiritu aModelo(Espiritu espiritu){
        espiritu.setCoordenada(new Coordenada(this.getCoordenada().getX(), this.getCoordenada().getY()));
        if (espiritu.getMedium() != null) espiritu.getMedium().setCoordenada(new Coordenada(espiritu.getCoordenada().getX(), espiritu.getCoordenada().getY()));
        return espiritu;
    }

}
