package ar.edu.unq.epersgeist.modelo.espiritu;

import jakarta.persistence.Entity;

@Entity
public class Angel extends Espiritu {
    public Angel(String nombre) {
        super(nombre);
    }
    public Angel( Integer nivelDeConexion, String nombre) {
        super(nivelDeConexion, nombre);
    }

    public void atacar(Demonio demonio) {
        //hacer algo
        demonio.defender(this);
    }
}
