package ar.edu.unq.epersgeist.controller.dto.EspirituDTO;

import ar.edu.unq.epersgeist.modelo.Coordenada;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.exception.ParametroIncorrectoException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;

import java.util.List;


public record EspirituDTOPostRequest(TipoEspiritu tipo, String nombre, int nivelDeConexion, List<Double> coordenada){

    public Espiritu aModelo() {
        Integer nivelDeConexionInteger = nivelDeConexion;
        if (this.nombre == null) throw new ParametroIncorrectoException("El nombre no debe ser nulo.");
        if (this.tipo == null) throw new ParametroIncorrectoException("El tipo del espíritu no debe ser nulo.");

        if (coordenada.size()!=2) throw new ParametroIncorrectoException("La coordenada debe estar compuesta por 2 valores numericos, \"X\" e \"Y\".");

        Coordenada coord = new Coordenada(coordenada.get(0),coordenada.get(1));

        return this.tipo.getEspirituInstance(this.nivelDeConexion, this.nombre, coord);
    }

}
