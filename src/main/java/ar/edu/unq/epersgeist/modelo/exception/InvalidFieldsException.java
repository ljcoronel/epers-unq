package ar.edu.unq.epersgeist.modelo.exception;

public class InvalidFieldsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidFieldsException(String nombreDeLaClase) {

        super("Algunos de los campos ingresados son invalidos en la clase "+nombreDeLaClase+".");
  }
}
