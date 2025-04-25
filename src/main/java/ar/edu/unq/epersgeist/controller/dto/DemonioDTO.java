package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;

public record DemonioDTO(Long id, Integer nivelDeConexion, String nombre, MediumDTO medium, Ubicacion ubicacion) {

    public static DemonioDTO desdeModelo(Demonio demonio){
        return null;
    }

    public Demonio aModelo(){
        return null;
    }
}
