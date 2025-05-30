package ar.edu.unq.epersgeist.modelo.exception;

public class EntidadInexistenteException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntidadInexistenteException(String mensaje) {

        super(mensaje);
    }
}
