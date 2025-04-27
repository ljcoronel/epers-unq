package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;

public record UbicacionDTO(Long id,String nombre) {
    public static UbicacionDTO desdeModelo(Long id, String nombre) {
        return new UbicacionDTO(1L,"Bosque de los Lamentos");//solo para pruebas, borrar despues
    }

    public Ubicacion aModelo() {
        return null;
    }
}