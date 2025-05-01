package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Ubicacion;

public record UbicacionDTO(Long id,String nombre) {

    public static UbicacionDTO desdeModelo(Ubicacion ubicacion) {
        return new UbicacionDTO(
                ubicacion.getId(),
                ubicacion.getNombre());
    }

    public Ubicacion aModelo(){
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setId(this.id());
        ubicacion.setNombre(this.nombre());

        return ubicacion;
    }
}