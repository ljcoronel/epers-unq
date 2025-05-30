package ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.exception.ParametroIncorrectoException;

public record UbicacionDTOPostRequest(TipoUbicacion tipo, String nombre, int energia) {

    public Ubicacion aModelo() {
        if (this.tipo == null) throw new ParametroIncorrectoException("La ubicacion debe tener un tipo");
        if (this.nombre == null) throw new ParametroIncorrectoException("La ubicación debe tener un nombre.");

        return this.tipo.getUbicacionInstance(this.nombre, this.energia );
    }

}
