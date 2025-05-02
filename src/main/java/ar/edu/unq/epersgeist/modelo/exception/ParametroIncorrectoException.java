package ar.edu.unq.epersgeist.modelo.exception;

public class ParametroIncorrectoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ParametroIncorrectoException(String mensaje) {
        super(mensaje);
    }
}
