package ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.exception.UbicacionRequestSinNombreException;

public record UbicacionDTORequest(String nombre) {

    public static UbicacionDTORequest desdeModelo(Ubicacion ubicacion) {
        if(ubicacion.getNombre() == null) throw new UbicacionRequestSinNombreException();
        return new UbicacionDTORequest(ubicacion.getNombre());
    }

    public Ubicacion aModelo() {
        if(this.nombre == null) throw new UbicacionRequestSinNombreException();
        return new Ubicacion(this.nombre);
    }

    public Ubicacion aModelo(Long id) {
        if(this.nombre == null) throw new UbicacionRequestSinNombreException();
        Ubicacion ubicacion = new Ubicacion(this.nombre);
        ubicacion.setId(id);
        return ubicacion;
    }
}
