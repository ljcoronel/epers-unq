package ar.edu.unq.epersgeist.controller.dto;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Getter
public class DTOResponseError {

    private HttpStatusCode statusError;
    private int statusCode;
    private String mensaje;
    public LocalDateTime date;

    public DTOResponseError(HttpStatusCode statusError, String mensaje) {
        this.statusError = statusError;
        this.statusCode = statusError.value();
        this.mensaje = mensaje;
        this.date =  LocalDateTime.now();;
    }

}
