package ar.edu.unq.epersgeist.modelo.espiritu;

import ar.edu.unq.epersgeist.modelo.Coordenada;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.EfectosDeUbicacion;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor
@ToString
public class Demonio extends Espiritu implements EfectosDeUbicacion  {

    public Demonio(Integer nivelDeConexion, String nombre, Ubicacion ubicacion) {//se usa en el aModelo del JPA
        super(nivelDeConexion, nombre, ubicacion);
    }

    public Demonio(String nombre, Coordenada coordenada) {
        super(nombre, coordenada);
    }

    public Demonio(Integer nivelDeConexion, String nombre, Coordenada coordenada,Ubicacion ubicacion) {
        super(nivelDeConexion,nombre, coordenada,ubicacion);
    }

    public void defender(Angel angel,int ataque) {

        int porcentajeDeDefensa = this.porcentajeDefensaRandom();

        if(ataque>porcentajeDeDefensa){

           int daño = angel.getNivelDeConexion()/2;
           this.recibirDamage(daño);

        }else{
            angel.recibirDamage(5);
        }
    }

    @Override
    public void agregarAEspiritusDeMedium(Medium medium) {
        medium.agregarADemonios(this);
    }

    @Override
    public void desconectarDeMedium(Medium medium) {
        medium.quitarDeDemonios(this);
    }

    @Override
    public void invocarASantuarioAMedium(Santuario santuario, Medium medium) {}

    @Override
    public void invocadoACementerioPorMedium(Cementerio cementerio, Medium medium) {
        serInvocadoPorMedium(cementerio, medium);
    }


    @Override
    public void recibirEfectoDe(Santuario ubicacion) {
        this.reducirNivelDeConexionEn(10);
    }
}
