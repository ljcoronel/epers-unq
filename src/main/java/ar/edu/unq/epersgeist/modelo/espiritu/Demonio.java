package ar.edu.unq.epersgeist.modelo.espiritu;

import ar.edu.unq.epersgeist.modelo.RandomGenerator.RandomGenerator;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor
@ToString

@Entity
public class Demonio extends Espiritu {
    public Demonio(String nombre, Ubicacion ubicacion) {
        super(nombre, ubicacion);
    }
    public Demonio(Integer nivelDeConexion, String nombre, Ubicacion ubicacion) {
        super(nivelDeConexion, nombre, ubicacion);
    }


    public void defender(Angel angel,int ataque) {

        int procentajeDeDefensa = this.porcentajeDefensaRandom();

        if(ataque>procentajeDeDefensa){

           int daño = angel.getNivelDeConexion()/2;
           this.recibirDamage(daño);

        }else{
            angel.recibirDamage(5);
        }
    }
    @Override
    public boolean esDemonio(){
        return true;
    }


}
