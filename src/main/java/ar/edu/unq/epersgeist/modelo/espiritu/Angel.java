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

@Getter @Setter @NoArgsConstructor @ToString
public class Angel extends Espiritu implements EfectosDeUbicacion {

    public Angel(Integer nivelDeConexion, String nombre, Ubicacion ubicacion) {//TODO borrar?
        super(nivelDeConexion,nombre, ubicacion);
    }

    public Angel(Integer nivelDeConexion, String nombre, Point coordenada,Ubicacion ubicacion) {//TODO borrar ubicacion
        super(nivelDeConexion,nombre, coordenada,ubicacion);
    }

    public Angel(String nombre, Point coordenada) {
        super(nombre, coordenada);
    }

    public Angel(int nivelDeConexion, String nombre) {
        super(nivelDeConexion, nombre);
    }

    public void atacar(Demonio demonio) {

        int porcentajeDeAtaque = this.porcentajeAtaqueRandom();

        demonio.defender(this,porcentajeDeAtaque);
    }

    @Override
    public void agregarAEspiritusDeMedium(Medium medium) {
        medium.agregarAAngeles(this);
    }

    @Override
    public void desconectarDeMedium(Medium medium) {
        medium.quitarDeAngeles(this);
    }

    @Override
    public void invocarASantuarioAMedium(Santuario santuario, Medium medium) {
        medium.usarManaParaInvocar();
        this.mover(santuario);
    }

    @Override
    public void invocadoACementerioPorMedium(Cementerio cementerio, Medium medium) {}

    @Override
    public void recibirEfectoDe(Cementerio ubicacion) {

        this.reducirNivelDeConexionEn(5);

    }

}
