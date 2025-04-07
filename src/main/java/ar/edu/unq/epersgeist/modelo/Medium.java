package ar.edu.unq.epersgeist.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Medium implements Serializable {

    @Id
    private Long id;
    private String nombre;
    private Integer manaMax;
    private Integer mana;

    @OneToMany
    private Set<Espiritu> espiritus = new HashSet<>();

    @ManyToOne
    private Ubicacion ubicacion;

    public Medium(String nombre, Integer manaMax, Integer mana) {
        this.nombre = nombre;
        this.manaMax = manaMax;
        this.mana = mana;
    }

    public void conectarseAEspiritu(Espiritu espiritu) {
        this.espiritus.add(espiritu);
        espiritu.aumentarConexion(this);
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getManaMax() {
        return manaMax;
    }

    public Integer getMana() {
        return mana;
    }

    public Set<Espiritu> getEspiritus() {
        return espiritus;
    }
}