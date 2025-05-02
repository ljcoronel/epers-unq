package ar.edu.unq.epersgeist.controller.dto.EspirituDTO;

import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;


public record EspirituDTORequest(TipoEspiritu tipo, String nombre, int nivelDeConexion, Long ubicacionId) {

    public static EspirituDTORequest desdeModelo(Espiritu espiritu) {
        return new EspirituDTORequest(
                TipoEspiritu.getValueOfEspiritu(espiritu),
                espiritu.getNombre(),
                espiritu.getNivelDeConexion(),
                espiritu.getUbicacion().getId()
        );
    }

    public Espiritu aModelo(Ubicacion ubicacion) {

        return this.tipo.getEspirituInstance(this.nivelDeConexion, this.nombre, ubicacion);
    }

}
