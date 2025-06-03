package ar.edu.unq.epersgeist.persistencia.repositories.dto.mongo;
import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.TipoEspiritu;
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
    private String nombre;//TODO borrar al terminar de testear
    private GeoJsonPoint coordenada;
    private Long idUbicacion;

    public EspirituMongoDTO(Espiritu espiritu) {
        this.id = espiritu.getId();
        this.nombre = espiritu.getNombre();//TODO borrar al terminar de testear
        this.coordenada = new GeoJsonPoint(espiritu.getCoordenada().getX(), espiritu.getCoordenada().getY());
        this.idUbicacion = espiritu.getUbicacion().getId();
    }

    public static EspirituMongoDTO desdeModelo(Espiritu espiritu){
        TipoEspiritu tipo = TipoEspiritu.getValueOfEspiritu(espiritu);
        return tipo.getMongo(espiritu);
    }

    public Espiritu aModelo(Espiritu espiritu){
        espiritu.setCoordenada(this.getCoordenada());
        return espiritu;
    }

}
