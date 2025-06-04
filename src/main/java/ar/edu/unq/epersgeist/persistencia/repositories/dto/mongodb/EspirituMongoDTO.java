package ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter

@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Document("Espiritu")
public class EspirituMongoDTO {

    @Id
    private Long id;
    private GeoJsonPoint coordenada;
    private Long idUbicacion;

    public EspirituMongoDTO(Espiritu espiritu) {
        this.id = espiritu.getId();
        this.coordenada = new GeoJsonPoint(espiritu.getCoordenada().getX(), espiritu.getCoordenada().getY());
        this.idUbicacion = espiritu.getUbicacion().getId();
    }

    public static EspirituMongoDTO desdeModelo(Espiritu espiritu){
        return new EspirituMongoDTO(espiritu);
    }

    public Espiritu aModelo(Espiritu espiritu){
        espiritu.setCoordenada(this.getCoordenada());
        if (espiritu.getMedium() != null) espiritu.getMedium().setCoordenada(espiritu.getCoordenada());
        return espiritu;
    }

}
