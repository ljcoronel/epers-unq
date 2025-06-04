package ar.edu.unq.epersgeist.persistencia.repositories.dto.mongo;


import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Optional;


@Getter @Setter

@Document("Ubicacion")
public class UbicacionMONGODTO {

    @Id
    private Long id;
    private GeoJsonPolygon area;

    public UbicacionMONGODTO(Long id, GeoJsonPolygon area) {
        this.id = id;
        this.area = area;
    }

    public static UbicacionMONGODTO desdeModelo(Ubicacion ubicacion) {
        GeoJsonPolygon area = new GeoJsonPolygon(ubicacion.getTerritorio());
        return new UbicacionMONGODTO(ubicacion.getId(), area );
    }

    public List<Point> getPoints() {
        return this.area.getPoints();
    }

    public Ubicacion aModelo(Ubicacion ubicacion) {
        ubicacion.setTerritorio(this.getPoints());
        return ubicacion;
    }
}

