package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Ubicacion;

public record UbicacionDTORequest(String nombre) {

    public static UbicacionDTORequest desdeModelo(Ubicacion ubicacion) {
        return new UbicacionDTORequest(ubicacion.getNombre());
    }

    public Ubicacion aModelo() {
        return new Ubicacion(this.nombre);
    }

    public Ubicacion aModelo(Long id) {
        Ubicacion ubicacion = new Ubicacion(this.nombre);
        ubicacion.setId(id);
        return ubicacion;
    }
}
