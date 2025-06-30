package ar.edu.unq.epersgeist.modelo.espiritu;

import ar.edu.unq.epersgeist.modelo.geo.Coordenada;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.EfectosDeUbicacion;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.ubicacion.UbicacionJPADTO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
public class Angel extends Espiritu implements EfectosDeUbicacion {

    public Angel(Integer nivelDeConexion, String nombre ) {//se usa en el aModelo del JPA
        super(nivelDeConexion,nombre);
    }

    public Angel(Integer nivelDeConexion, String nombre, Coordenada coordenada ) {
        super(nivelDeConexion,nombre, coordenada);
    }

    public Angel(String nombre, Coordenada coordenada) {
        super(nombre, coordenada);
    }

    public Angel(int nivelDeConexion, String nombre) {
        super(nivelDeConexion, nombre);
    }

    public Angel(Integer nivelDeConexion, String nombre, Ubicacion ubicacion) {
        super(nivelDeConexion,nombre, ubicacion);
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
        serInvocadoPorMedium(santuario, medium);
    }

    @Override
    public void invocadoACementerioPorMedium(Cementerio cementerio, Medium medium) {}

    @Override
    public void recibirEfectoDe(Cementerio ubicacion) {
        this.reducirNivelDeConexionEn(5);
    }


    @Override
    public void actualizarTempAntes() {
        // TODO bajar temperatura si es necesario
    }

    @Override
    public void actualizarTempDespues(Ubicacion ubicacion) {
        // TODO subir temperatura si es necesario
    }

}
