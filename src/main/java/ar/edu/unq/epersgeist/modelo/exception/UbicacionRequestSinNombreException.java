package ar.edu.unq.epersgeist.modelo.exception;

public class UbicacionRequestSinNombreException extends RequestBodyIncompletoException {
    public UbicacionRequestSinNombreException() {
        super("La ubicación debe tener un nombre");
    }
}
