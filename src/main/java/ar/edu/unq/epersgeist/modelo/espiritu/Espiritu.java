package ar.edu.unq.epersgeist.modelo.espiritu;


import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.RandomGenerator.RandomGenerator;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.exception.EspirituLibreException;
import ar.edu.unq.epersgeist.modelo.exception.UbicacionDistintaException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Check;

import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @ToString


@Entity
// singletable, por el momento tiene sentido porque ambos tipos de espíritu sólo cambian en comportamiento, se mantiene la misma estructura.
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Espiritu implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @Check(name = "valoresValidosDeConexion", constraints = "nivel_de_conexion BETWEEN 0 AND 100")
    private Integer nivelDeConexion;
    @Column(unique = true, length = 50, nullable = false)
    private String nombre;
    @ManyToOne
    private Medium medium;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Ubicacion ubicacion;

    public Espiritu(Integer nivelDeConexion, String nombre, Ubicacion ubicacion) {

        this.setNivelDeConexion(nivelDeConexion); //para evitar valores por bajo de 0 y por encima de 100
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

    public Espiritu(String nombre, Ubicacion ubicacion) {

        this.setNivelDeConexion(0); //default es 0
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

    public Espiritu(String nombre, int nivelDeConexion) {

        this.setNivelDeConexion(0); //default es 0
        this.nombre = nombre;
        this.nivelDeConexion = nivelDeConexion;
    }


    public void aumentarNivelDeConexionEn(int cantConexion) {
        this.setNivelDeConexion(this.getNivelDeConexion() + cantConexion);
    }

    public void reducirNivelDeConexionEn(int i) {
        int nivel = getNivelDeConexion();
        setNivelDeConexion(nivel - i);
    }

    public void setNivelDeConexion(int i) {
        if (i < 0) {
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
        if(this.getNivelDeConexion()==0){
            this.desvincularseDeMedium();
        }

    }

    private void desvincularseDeMedium(){

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

    public void setUbicacion(Ubicacion ubicacion) {
        if (this.estaLibre()) this.ubicacion = ubicacion;
    }

    public void moverConMediumHacia(Medium medium, Ubicacion ubicacion) {
        if (this.getMedium() == medium) {
            this.ubicacion = ubicacion;
        }
    }

    public void conectarConMedium(Medium medium) {
        verificarMediumEnMismaUbicacion(medium);
        verificarEspirituLibre();

        int conexionAAumentar =(int) Math.round(medium.getMana()* getPorcentajeDeMana()) ;
        this.aumentarNivelDeConexionEn(conexionAAumentar);
        this.setMedium(medium);

        medium.realizarConexion(this);
    }

    private void verificarMediumEnMismaUbicacion(Medium medium) throws UbicacionDistintaException {
        if (!(this.getUbicacion() == medium.getUbicacion())){
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

}