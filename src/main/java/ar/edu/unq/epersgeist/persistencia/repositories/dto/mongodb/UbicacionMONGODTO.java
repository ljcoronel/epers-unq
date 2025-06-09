package ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb;

import ar.edu.unq.epersgeist.modelo.Coordenada;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@Document("Ubicacion")
public class UbicacionMONGODTO {

    @Id
    private Long id;
    @GeoSpatialIndexed(name = "area", type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPolygon area;

    public UbicacionMONGODTO(Long id, GeoJsonPolygon area) {
        this.id = id;
        this.area = area;
    }

    public static UbicacionMONGODTO desdeModelo(Ubicacion ubicacion) {
        GeoJsonPolygon area = new GeoJsonPolygon(ubicacion.getCoordenadas());
        return new UbicacionMONGODTO(ubicacion.getId(), area );
    }

    public List<Point> getPoints() {
        return this.area.getPoints();
    }

    public Ubicacion aModelo(Ubicacion ubicacion) {
        ubicacion.setTerritorio(this.getPoints().stream()
                .map(punto -> new Coordenada(punto.getX(), punto.getY()))
                .collect(Collectors.toList()));
        return ubicacion;
    }
}

