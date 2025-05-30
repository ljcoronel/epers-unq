package ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;

import ar.edu.unq.epersgeist.modelo.exception.ParametroIncorrectoException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

public record UbicacionDTOPutRequest (String nombre, int energia){

    public Ubicacion aModelo(Long id) {
        if (this.nombre == null) throw new ParametroIncorrectoException("La ubicación debe tener un nombre.");

        Ubicacion ubicacion = new Cementerio(this.nombre, this.energia);
        // un placeholder, la subclase concreta es irrelevante porque no se puede modificar tipo
        ubicacion.setId(id);

        return ubicacion;
    }
}
