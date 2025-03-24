package ar.edu.unq.epersgeist.modelo;

import java.io.Serializable;

public class Espiritu implements Serializable {

    private Long id;
    private String tipo;
    private Integer nivelDeConexion;
    private String nombre;

    public Espiritu(String tipo, Integer nivelDeConexion, String nombre) {
        this.tipo = tipo;
        this.nivelDeConexion = nivelDeConexion;
        this.nombre = nombre;
    }

    public void aumentarConexion(Medium medium) {
        this.aumentarNivelDeConexionEn(10);
    }

    private void aumentarNivelDeConexionEn(int i) {
        int nivel = getNivelDeConexion();
        if (nivel + i < 0) {
            setNivelDeConexion(0);
        } else if (nivel + i > 100) {
            setNivelDeConexion(100);
        } else {
            setNivelDeConexion(i);
        }
    }

    private void setNivelDeConexion(int i) {
        this.nivelDeConexion = i;
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