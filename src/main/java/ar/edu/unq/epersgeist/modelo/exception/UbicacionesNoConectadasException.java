package ar.edu.unq.epersgeist.modelo.exception;

public class UbicacionesNoConectadasException extends RuntimeException {
    public UbicacionesNoConectadasException() {
        super("las Ubicaciones no estan conectadas");
    }
}
