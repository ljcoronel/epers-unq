package ar.edu.unq.epersgeist.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Espiritu implements Serializable {

    @Id
    private Long id;
    private String tipo;
    private Integer nivelDeConexion;
    private String nombre;

    @ManyToOne
    private Ubicacion ubicacion;

    public Espiritu(String tipo, Integer nivelDeConexion, String nombre) {
        this.tipo = tipo;
        this.setNivelDeConexion(nivelDeConexion); //para evitar valores por bajo de 0 y por encima de 100
        this.nombre = nombre;
    }

    public Espiritu(String tipo, String nombre) {
        this.tipo = tipo;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public Integer getNivelDeConexion() {
        return nivelDeConexion;
    }

    public String getNombre() {
        return nombre;
    }
}