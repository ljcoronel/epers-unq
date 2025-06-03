package ar.edu.unq.epersgeist.modelo.espiritu;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.RandomGenerator.RandomGenerator;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.EfectosDeUbicacion;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.exception.EntidadInexistenteException;
import ar.edu.unq.epersgeist.modelo.exception.EspirituLibreException;
import ar.edu.unq.epersgeist.modelo.exception.UbicacionDistintaException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.geo.Point;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter @Setter @NoArgsConstructor @ToString
public abstract class Espiritu implements Serializable, EfectosDeUbicacion {

    private Long id;
    private Integer nivelDeConexion;
    private String nombre;

    private Medium medium;
    private Ubicacion ubicacion;
    private Point coordenada;

    public Espiritu(Integer nivelDeConexion, String nombre, Ubicacion ubicacion) {//TODO borrar a la mierda

        this.setNivelDeConexion(nivelDeConexion);
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.coordenada = new Point(33,24);
    }

    public Espiritu(Integer nivelDeConexion, String nombre, Point coordenada) {

        this.setNivelDeConexion(nivelDeConexion); //para evitar valores por bajo de 0 y por encima de 100
        this.nombre = nombre;
        this.coordenada = coordenada;
    }

    public Espiritu(String nombre, Point coordenada) {

        this.setNivelDeConexion(0); //default es 0
        this.nombre = nombre;
        this.coordenada = coordenada;
    }


    public Espiritu(String nombre, Ubicacion ubicacion) {//TODO borrar a la mierda

        this.setNivelDeConexion(0); //default es 0
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

    public Espiritu(int nivelDeConexion, String nombre) {
        this.nombre = nombre;
        setNivelDeConexion(nivelDeConexion);
    }


    public void aumentarNivelDeConexionEn(int cantConexion) {
        this.setNivelDeConexion(this.getNivelDeConexion() + cantConexion);
    }

    public void reducirNivelDeConexionEn(int i) {
        int nivel = getNivelDeConexion();
        setNivelDeConexion(nivel - i);
        if (this.getNivelDeConexion() == 0) this.desvincularseDeMedium();
    }

    public void setNivelDeConexion(int i) {
        if (i <= 0) {
            this.nivelDeConexion = 0;
        } else if (i > 100) {
            this.nivelDeConexion = 100;
        } else {
            this.nivelDeConexion = i;
        }

    }

    public boolean estaLibre(){
        return this.medium == null;
    }

    protected void recibirDamage(int ataque){
        this.reducirNivelDeConexionEn(ataque);

    }

    private void desvincularseDeMedium(){

        if (medium == null) return;

        medium.desvincularseDeEspiritu(this);
        this.setMedium(null);
    }

    protected int porcentajeAtaqueRandom(){
        int porcentajeDeExito = RandomGenerator.getInstance().getRandom(1, 10) + this.getNivelDeConexion();
        return Math.min(porcentajeDeExito, 100);
    }

    protected int porcentajeDefensaRandom(){
        return RandomGenerator.getInstance().getRandom(1, 100);
    }

    public void mover(Ubicacion ubicacion) {
        if (this.estaLibre()) this.setUbicacion(ubicacion);
    }

    public void moverConMediumHacia(Ubicacion ubicacion) {

            ubicacion.recibir(this);
            this.ubicacion = ubicacion;

    }

    public void conectarConMedium(Optional<Medium> optionalMedium) {
        Medium medium = optionalMedium.orElseThrow(() -> new EntidadInexistenteException("No se encontró al Médium"));

        verificarMediumEnMismaUbicacion(medium);
        verificarEspirituLibre();

        int conexionAAumentar =(int) Math.round(medium.getMana()* getPorcentajeDeMana()) ;
        this.aumentarNivelDeConexionEn(conexionAAumentar);
        this.setMedium(medium);

        medium.realizarConexion(this);
    }

    private void verificarMediumEnMismaUbicacion(Medium medium) throws UbicacionDistintaException {
        if (!(Objects.equals(this.getUbicacion().getId(), medium.getUbicacion().getId()))){
            throw new UbicacionDistintaException(this, medium);
        }
    }

    private void verificarEspirituLibre() {
        if (!this.estaLibre()) {
            throw new EspirituLibreException(this);
        }
    }

    private static double getPorcentajeDeMana() {
        return 0.20;
    }

    abstract public void agregarAEspiritusDeMedium(Medium medium);

    abstract public void desconectarDeMedium(Medium medium);

    public abstract void invocarASantuarioAMedium(Santuario santuario, Medium medium);

    public abstract void invocadoACementerioPorMedium(Cementerio cementerio, Medium medium);

    @Override
    public void recibirEfectoDe(Cementerio ubicacion) {}

    @Override
    public void recibirEfectoDe(Santuario ubicacion) {}


    }
