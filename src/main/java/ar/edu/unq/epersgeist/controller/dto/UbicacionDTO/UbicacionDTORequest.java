package ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.exception.ParametroIncorrectoException;
import lombok.Builder;

public record UbicacionDTORequest(TipoUbicacion tipoUbicacion, String nombre, int energia) {

    public Ubicacion aModelo() {
        if (this.tipoUbicacion == null) throw new ParametroIncorrectoException("La ubicacion debe tener un tipo");
        if (this.nombre == null) throw new ParametroIncorrectoException("La ubicación debe tener un nombre.");

        //        return new Ubicacion(this.nombre);
        return this.tipoUbicacion().getUbicacionInstance(this.nombre, this.energia );
    }

    public Ubicacion aModelo(Long id) {
        if (this.nombre == null) throw new ParametroIncorrectoException("La ubicación debe tener un nombre.");
        Ubicacion ubicacion = this.tipoUbicacion.getUbicacionInstance(this.nombre, this.energia);
        ubicacion.setId(id);
        return ubicacion;
    }
}
