package ar.edu.unq.epersgeist.controller.dto.EspirituDTO;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class DtoResponseError {

    private HttpStatusCode statusCode;
    private String mensaje;
    public LocalDateTime date;

    public DtoResponseError (HttpStatusCode statusCode, String mensaje) {
        this.statusCode = statusCode;
        this.mensaje = mensaje;
        this.date =  LocalDateTime.now();;
    }

}
