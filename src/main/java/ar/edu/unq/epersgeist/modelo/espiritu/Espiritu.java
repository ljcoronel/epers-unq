package ar.edu.unq.epersgeist.modelo.espiritu;


import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Check;

import java.io.Serializable;
import java.util.List;

@Getter @Setter @NoArgsConstructor @ToString


@Entity
// singletable, por el momento tiene sentido porque ambos tipos de espíritu sólo cambian en comportamiento, se mantiene la misma estructura
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Espiritu implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @Check(name = "valoresValidosDeConexion", constraints = "nivelDeConexion BETWEEN 0 AND 100")
    private Integer nivelDeConexion;
    @Column(nullable = false, unique = true, length = 50)
    private String nombre;
    @ManyToOne
    private Medium medium;


    public Espiritu(Integer nivelDeConexion, String nombre, Ubicacion ubicacion) {

        this.setNivelDeConexion(nivelDeConexion); //para evitar valores por bajo de 0 y por encima de 100
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

    public Espiritu(String nombre, Ubicacion ubicacion) {

        this.setNivelDeConexion(0); //default es 0
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }


    public void aumentarNivelDeConexionEn(int cantConexion) {
        this.setNivelDeConexion(this.getNivelDeConexion() + cantConexion);
    }

    private void setNivelDeConexion(int i) {
        if (i < 0) {
            this.nivelDeConexion = 0;
        } else if (i > 100) {
            this.nivelDeConexion = 100;
        } else {
            this.nivelDeConexion = i;
        }

    }


    public boolean estaLibre(){
        return this.medium == null;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    private Ubicacion ubicacion;

}