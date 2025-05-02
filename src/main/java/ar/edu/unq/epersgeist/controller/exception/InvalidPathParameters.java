package ar.edu.unq.epersgeist.controller.exception;

import org.springframework.http.HttpStatus;

public class InvalidPathParameters extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private HttpStatus status;

    public InvalidPathParameters(String mensaje, HttpStatus status) {
        super(mensaje);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

}

