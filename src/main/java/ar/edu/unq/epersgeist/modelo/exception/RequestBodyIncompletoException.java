package ar.edu.unq.epersgeist.modelo.exception;

public class RequestBodyIncompletoException extends RuntimeException {
  public RequestBodyIncompletoException(String mensaje) {
    super(mensaje);
  }
}
