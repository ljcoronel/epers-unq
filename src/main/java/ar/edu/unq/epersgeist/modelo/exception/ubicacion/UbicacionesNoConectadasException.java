package ar.edu.unq.epersgeist.modelo.exception.ubicacion;

public class UbicacionesNoConectadasException extends RuntimeException {
    public UbicacionesNoConectadasException() {
        super("las Ubicaciones no estan conectadas");
    }
}
