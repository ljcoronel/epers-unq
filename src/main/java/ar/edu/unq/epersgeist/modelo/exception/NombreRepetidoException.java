package ar.edu.unq.epersgeist.modelo.exception;

public class NombreRepetidoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NombreRepetidoException(String mensaje) {
        //        super("El nombre de la ubicacion: [" + nombre + "] ya esta siendo utilizado y no puede volver a crearse");
        super(mensaje);
    }

    // TODO poner el mensaje de ubicación de vuelta
}
