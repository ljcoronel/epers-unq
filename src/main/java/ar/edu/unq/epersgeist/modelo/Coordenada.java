package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.FueraDelMapaException;
import ar.edu.unq.epersgeist.modelo.exception.ParametroIncorrectoException;
import lombok.EqualsAndHashCode;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import java.util.List;


public class Coordenada extends Point {

    private Double longitud;
    private Double latitud;

    public Coordenada(double longitud, double latitud) {
        super(longitud, latitud);
        this.verificarLongitud(longitud);
        this.verificarLatitud(latitud);
    }

    public Coordenada(List<Double> coordenadas) {
        this(coordenadas.get(0), coordenadas.get(1));
    }


    private void verificarLongitud(double longitud) {
        if (Math.abs(longitud) > 180.0) throw new FueraDelMapaException(this.mensajeDeErrorLongitudExcedida());
    }

    private void verificarLatitud(double latitud) {
        if (Math.abs(latitud) > 90.0) throw new FueraDelMapaException(this.mensajeDeErrorLatitudExcedida());
    }

    private String mensajeDeErrorLongitudExcedida() {
        return "La longitud no puede ser mayor a 180 ni menor a -180";
    }

    private String mensajeDeErrorLatitudExcedida() {
        return "La latitud no puede ser mayor a 90 ni menor a -90";
    }
}
