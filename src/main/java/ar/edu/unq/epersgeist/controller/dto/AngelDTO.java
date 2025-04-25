package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;

public record AngelDTO(Long id, Integer nivelDeConexion, String nombre, MediumDTO medium, Ubicacion ubicacion) {

    public static AngelDTO desdeModelo(Angel angel){
        return null;
    }

    public Angel aModelo(){
        return null;
    }

}
