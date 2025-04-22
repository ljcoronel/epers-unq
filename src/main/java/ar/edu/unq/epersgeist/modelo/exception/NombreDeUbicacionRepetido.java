package ar.edu.unq.epersgeist.modelo.exception;

public class NombreDeUbicacionRepetido extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NombreDeUbicacionRepetido(String nombre) {
        super("El nombre de la ubicacion: [" + nombre + "] ya esta siendo utilizado y no puede volver a crearse");
    }
}
