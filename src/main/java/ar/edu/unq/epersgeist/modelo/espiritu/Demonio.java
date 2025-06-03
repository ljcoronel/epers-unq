package ar.edu.unq.epersgeist.modelo.espiritu;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.EfectosDeUbicacion;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.geo.Point;

@Getter @Setter @NoArgsConstructor
@ToString
public class Demonio extends Espiritu implements EfectosDeUbicacion  {
    public Demonio(String nombre, Ubicacion ubicacion) {
        super(nombre, ubicacion);
    }//TODO borrar a la mierda

    public Demonio(Integer nivelDeConexion, String nombre, Ubicacion ubicacion) {//TODO borrar a la mierda
        super(nivelDeConexion, nombre, ubicacion);
    }

    public Demonio(String nombre, Point coordenada) {
        super(nombre, coordenada);
    }

    public Demonio(Integer nivelDeConexion, String nombre, Point coordenada) {
        super(nivelDeConexion,nombre, coordenada);
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
        medium.usarManaParaInvocar();
        this.mover(cementerio);
    }


    @Override
    public void recibirEfectoDe(Santuario ubicacion) {
        this.reducirNivelDeConexionEn(10);
    }
}
