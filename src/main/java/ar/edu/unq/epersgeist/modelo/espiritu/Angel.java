package ar.edu.unq.epersgeist.modelo.espiritu;

import ar.edu.unq.epersgeist.modelo.RandomGenerator.RandomGenerator;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString

@Entity
public class Angel extends Espiritu {
    public Angel(String nombre) {
        super(nombre);
    }

    public Angel(Integer nivelDeConexion, String nombre) {
        super(nivelDeConexion,nombre);
    }

    public Angel(Integer nivelDeConexion, String nombre, RandomGenerator randomGenerator) {
        super(nivelDeConexion,nombre);
        this.setRandomGenerator(randomGenerator);
    }


    public void atacar(Demonio demonio) {

        int procentajeDeAtaque = this.porcentajeAtaqueRandom();

        demonio.defender(this,procentajeDeAtaque);
    }
    @Override
    public boolean esAngel(){
        return true;
    }

}
