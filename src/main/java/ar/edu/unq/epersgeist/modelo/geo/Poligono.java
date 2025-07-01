package ar.edu.unq.epersgeist.modelo.geo;

import ar.edu.unq.epersgeist.modelo.exception.PoligonoNoValidoException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.geo.Point;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor @ToString
public class Poligono {
    private List<Point> coordenadas;

    public Poligono(List<Coordenada> coordenadas) {

        this.coordenadas = this.validarPoligono(coordenadas);
    }

    private List<Point> validarPoligono(List<Coordenada> coordenadas) {
        if (coordenadas.size() < 3){
            throw new PoligonoNoValidoException("Debe ingresar al menos 3 coordenadas");
        }

        return this.getPoligonoCerrado(coordenadas);
    }

    public List<Point> getPoligonoCerrado(List<Coordenada> coordenadas) {

        List<Point> coordenadasValidadadas = new ArrayList<>(coordenadas);

        if (! (coordenadasValidadadas.getFirst().equals(coordenadasValidadadas.getLast()))){
            coordenadasValidadadas.add(coordenadas.getFirst());
        }
        return coordenadasValidadadas;
    }

}
