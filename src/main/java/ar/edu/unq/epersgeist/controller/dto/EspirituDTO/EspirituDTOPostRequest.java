package ar.edu.unq.epersgeist.controller.dto.EspirituDTO;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.exception.ParametroIncorrectoException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import org.springframework.data.geo.Point;

import java.util.List;


public record EspirituDTOPostRequest(TipoEspiritu tipo, String nombre, int nivelDeConexion, List<Double> coordenada){

    public Espiritu aModelo() {
        Integer nivelDeConexionInteger = nivelDeConexion;
        if (this.nombre == null) throw new ParametroIncorrectoException("El nombre no debe ser nulo.");
        if (this.tipo == null) throw new ParametroIncorrectoException("El tipo del espíritu no debe ser nulo.");

        Point coord = new Point(coordenada.get(0),coordenada.get(1));

        Ubicacion placeholder = new Santuario("placeholder",0,List.of(//TODO borrar placeholder al terminar el metodo mover
                new Point(0, 0),
                new Point(1, 0),
                new Point(1, 1),
                new Point(0, 1)
        ));

        return this.tipo.getEspirituInstance(this.nivelDeConexion, this.nombre, coord,placeholder);
    }

}
