package ar.edu.unq.epersgeist.controller.exception;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import org.springframework.http.HttpStatus;

public class invalidPathParameters extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private HttpStatus status;

    public invalidPathParameters(String mensaje, HttpStatus status) {
        super(mensaje);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

}

