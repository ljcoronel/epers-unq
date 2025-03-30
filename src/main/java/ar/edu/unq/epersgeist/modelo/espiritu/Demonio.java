package ar.edu.unq.epersgeist.modelo.espiritu;

import jakarta.persistence.Entity;

@Entity
public class Demonio extends Espiritu {
    public Demonio(String nombre) {
        super(nombre);
    }
    public Demonio(String nombre, Integer nivelDeConexion) {
        super(nivelDeConexion, nombre);
    }

    public void defender(Angel angel) {
        //hacer algo
    }
}
