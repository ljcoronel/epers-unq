package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @EqualsAndHashCode
@ToString

@Entity
public class Medium implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    @Check(constraints = "manaMax > 0")
    private Integer manaMax;
    @Column(nullable = false)
    @Check(constraints = "mana BETWEEN 0 AND manaMax")
    private Integer mana;
    @OneToMany
    private List<Espiritu> espiritus;
    @ManyToOne(fetch = FetchType.LAZY)
    private Ubicacion ubicacion;

    public Medium(String nombre, Integer manaMax, Integer mana) {
        this.nombre = nombre;
        this.manaMax = manaMax;
        this.mana = mana;
        this.espiritus = new ArrayList<>();
    }

    public void conectarseAEspiritu(Espiritu espiritu) {
        this.espiritus.add(espiritu);
        espiritu.aumentarConexion(this);
    }


}