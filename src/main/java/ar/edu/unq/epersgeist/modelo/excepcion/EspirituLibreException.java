package ar.edu.unq.epersgeist.modelo.excepcion;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;

public class EspirituLibreException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private Espiritu espiritu;

    public EspirituLibreException(Espiritu espiritu) {
        this.espiritu= espiritu;
    }

    @Override
    public String getMessage() {
        return "El Espiritu [" + espiritu.getNombre() + "] Es libre y no puede conectarse";
    }
}