package ar.edu.unq.epersgeist.modelo.exception.espiritu;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;

public class EspirituLibreException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private Espiritu espiritu;

    public EspirituLibreException(Espiritu espiritu) {
        this.espiritu= espiritu;
    }

    @Override
    public String getMessage() {
        return "El Espiritu [" + espiritu.getNombre() + "] no es libre y no puede conectarse";
    }
}