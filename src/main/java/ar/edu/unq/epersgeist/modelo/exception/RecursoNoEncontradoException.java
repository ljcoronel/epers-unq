package ar.edu.unq.epersgeist.modelo.exception;

public abstract class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
