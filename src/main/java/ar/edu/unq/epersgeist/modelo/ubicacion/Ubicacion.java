package ar.edu.unq.epersgeist.modelo.ubicacion;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor(access = AccessLevel.PUBLIC) @ToString

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Ubicacion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    public Ubicacion(String nombre) {
        this.nombre = nombre;
    }
}
