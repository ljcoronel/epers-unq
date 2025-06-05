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

@Getter @Setter @NoArgsConstructor @ToString
public class Angel extends Espiritu implements EfectosDeUbicacion {

    public Angel(Integer nivelDeConexion, String nombre, Ubicacion ubicacion) {//se usa en el aModelo del JPA
        super(nivelDeConexion,nombre, ubicacion);
    }

    public Angel(Integer nivelDeConexion, String nombre, Coordenada coordenada, Ubicacion ubicacion) {//TODO borrar ubicacion cuando se termine metodo mover
        super(nivelDeConexion,nombre, coordenada,ubicacion);
    }

    public Angel(String nombre, Coordenada coordenada) {
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
        this.setUbicacion(santuario);
    }

    @Override
    public void invocadoACementerioPorMedium(Cementerio cementerio, Medium medium) {}

    @Override
    public void recibirEfectoDe(Cementerio ubicacion) {

        this.reducirNivelDeConexionEn(5);

    }

}
