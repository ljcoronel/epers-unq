package ar.edu.unq.epersgeist.modelo.exception;

public class UbicacionNoEncontradaException extends RecursoNoEncontradoException {
    public UbicacionNoEncontradaException() {
        super("No se encontró la ubicación");
    }
}
