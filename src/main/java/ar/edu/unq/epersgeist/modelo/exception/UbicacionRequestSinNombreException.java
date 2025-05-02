package ar.edu.unq.epersgeist.modelo.exception;

public class UbicacionRequestSinNombreException extends RuntimeException {
    public UbicacionRequestSinNombreException() {
        super("La ubicación debe tener un nombre");
    }
}
