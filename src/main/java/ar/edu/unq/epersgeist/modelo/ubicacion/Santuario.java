package ar.edu.unq.epersgeist.modelo.ubicacion;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC) @ToString
@Entity
public class Santuario extends Ubicacion {

    public Santuario(String nombre) {
        super(nombre);
    }
}
