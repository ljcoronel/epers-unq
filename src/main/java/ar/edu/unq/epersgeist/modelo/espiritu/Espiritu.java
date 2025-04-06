package ar.edu.unq.epersgeist.modelo.espiritu;

import ar.edu.unq.epersgeist.modelo.Medium;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Check;

import java.io.Serializable;

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

    public Espiritu(Integer nivelDeConexion, String nombre) {

        this.setNivelDeConexion(nivelDeConexion); //para evitar valores por bajo de 0 y por encima de 100
        this.nombre = nombre;
    }

    public Espiritu(String nombre) {

        this.setNivelDeConexion(0); //default es 0
        this.nombre = nombre;
    }

    public void aumentarConexion(Medium medium) {

        this.aumentarNivelDeConexionEn(10);
    }

    private void aumentarNivelDeConexionEn(int i) {
        int nivel = getNivelDeConexion();
        setNivelDeConexion(i + nivel);
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

}