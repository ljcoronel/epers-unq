package ar.edu.unq.epersgeist.modelo.exception.ubicacion;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;

public class UbicacionDistintaException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private Espiritu espiritu;
    private Medium medium;

    public UbicacionDistintaException(Espiritu espiritu, Medium medium) {
        this.espiritu = espiritu;
        this.medium = medium;
    }

    @Override
    public String getMessage() {
        return "El espíritu: [" + espiritu.getNombre() + "] y el médium [" + medium.getNombre() + "] no se encuentran en la misma ubicación";
    }
}
