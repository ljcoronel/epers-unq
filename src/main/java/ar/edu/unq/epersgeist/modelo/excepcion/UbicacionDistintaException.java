package ar.edu.unq.epersgeist.modelo.excepcion;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;

public class UbicacionDistintaException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private Espiritu espiritu;
    private Medium medium;

    public UbicacionDistintaException(Espiritu espiritu, Medium medium) {
        this.espiritu= espiritu;
    }

    @Override
    public String getMessage() {
        return "El Espiritu: [" + espiritu.getNombre() + "] y el Medium [" + medium.getNombre() + "] no se encuentra en la misma ubicacion";

    }
}
