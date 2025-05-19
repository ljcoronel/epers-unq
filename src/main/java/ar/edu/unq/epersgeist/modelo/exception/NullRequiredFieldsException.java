package ar.edu.unq.epersgeist.modelo.exception;

public class NullRequiredFieldsException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public NullRequiredFieldsException(String nombreDeLaClase) {

      super("Hay campos requeridos nulos en la clase "+nombreDeLaClase+".");
  }
}
