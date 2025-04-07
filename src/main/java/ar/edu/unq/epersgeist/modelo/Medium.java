package ar.edu.unq.epersgeist.modelo;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @EqualsAndHashCode
@ToString

@Entity
public class Medium implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    @Check(constraints = "manaMax > 0")
    private Integer manaMax;
    @Column(nullable = false)
    @Check(constraints = "mana BETWEEN 0 AND manaMax")
    private Integer mana;
    //@OneToMany(mappedBy = "medium", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Espiritu> espiritus = new HashSet<>();

    public Medium(String nombre, Integer manaMax, Integer mana) {
        this.nombre = nombre;
        this.manaMax = manaMax;
        this.mana = mana;
    }

    public void conectarseAEspiritu(Espiritu espiritu) {
        this.espiritus.add(espiritu);
        espiritu.aumentarConexion(this);
    }


}