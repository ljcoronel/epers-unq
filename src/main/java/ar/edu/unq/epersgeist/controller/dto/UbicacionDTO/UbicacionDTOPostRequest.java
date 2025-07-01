package ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.exception.ParametroIncorrectoException;

import java.util.List;

public record UbicacionDTOPostRequest(TipoUbicacion tipo, String nombre, int energia, List<List<Double>> area, double temperaturaInicial) {

    public Ubicacion aModelo() {
        if (this.tipo == null) throw new ParametroIncorrectoException("La ubicacion debe tener un tipo");
        if (this.nombre == null) throw new ParametroIncorrectoException("La ubicación debe tener un nombre.");
        if (this.area == null) throw  new ParametroIncorrectoException("La ubicacion debe tener un area.");

        return this.tipo.getUbicacionInstance(this.nombre, this.energia, this.area, this.temperaturaInicial);
    }

}
