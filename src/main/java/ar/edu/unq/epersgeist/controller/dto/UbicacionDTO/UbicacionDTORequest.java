package ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.exception.ParametroIncorrectoException;

public record UbicacionDTORequest(String nombre) {

    public static UbicacionDTORequest desdeModelo(Ubicacion ubicacion) {
        if (ubicacion.getNombre() == null) throw new ParametroIncorrectoException("La ubicación debe tener un nombre.");
        return new UbicacionDTORequest(ubicacion.getNombre());
    }

    public Ubicacion aModelo() {
        if (this.nombre == null) throw new ParametroIncorrectoException("La ubicación debe tener un nombre.");
//        return new Ubicacion(this.nombre);
        return null;
    }

    public Ubicacion aModelo(Long id) {
        if (this.nombre == null) throw new ParametroIncorrectoException("La ubicación debe tener un nombre.");
//        Ubicacion ubicacion = new Ubicacion(this.nombre);
//        ubicacion.setId(id);
//        return ubicacion;
        return null;
    }
}
