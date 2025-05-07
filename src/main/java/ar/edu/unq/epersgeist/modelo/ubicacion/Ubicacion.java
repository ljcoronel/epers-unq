package ar.edu.unq.epersgeist.modelo.ubicacion;

import ar.edu.unq.epersgeist.modelo.EfectosDeUbicacion;
import ar.edu.unq.epersgeist.modelo.UbicacionesRecibidoras;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.NotImplementedException;

import java.io.Serializable;

@Getter @Setter @NoArgsConstructor(access = AccessLevel.PUBLIC) @ToString

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Ubicacion implements Serializable, UbicacionesRecibidoras {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    public Ubicacion(String nombre) {
        this.nombre = nombre;
    }


    @Override
    public void recibir(EfectosDeUbicacion espiritu) {
        throw new NotImplementedException("recibir no implementado");
    }
}
