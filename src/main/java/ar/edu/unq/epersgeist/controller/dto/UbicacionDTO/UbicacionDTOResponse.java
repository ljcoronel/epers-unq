package ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;

import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

public record UbicacionDTOResponse(Long id, String nombre) {

    public static UbicacionDTOResponse desdeModelo(Ubicacion ubicacion) {
        return new UbicacionDTOResponse(ubicacion.getId(), ubicacion.getNombre());
    }

    public Ubicacion aModelo() {
        Ubicacion ubicacion = new Santuario(this.nombre);
        ubicacion.setId(this.id);
        return ubicacion;
    }

    public Ubicacion aModelo(Long id) {
        Ubicacion ubicacion = new Santuario(this.nombre);
        ubicacion.setId(id);
        return ubicacion;
    }
}
