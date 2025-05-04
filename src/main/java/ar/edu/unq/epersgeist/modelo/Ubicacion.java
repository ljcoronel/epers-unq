package ar.edu.unq.epersgeist.modelo;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor(access = AccessLevel.PUBLIC) @ToString

@Entity
public class Ubicacion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    public Ubicacion(String nombre) {
        this.nombre = nombre;
    }
}
