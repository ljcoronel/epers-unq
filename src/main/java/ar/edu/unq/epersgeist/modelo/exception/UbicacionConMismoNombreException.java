package ar.edu.unq.epersgeist.modelo.exception;

public class UbicacionConMismoNombreException extends RuntimeException {
    public UbicacionConMismoNombreException() {
        super("No se puede crear una ubicación con nombre repetido");
    }
}
