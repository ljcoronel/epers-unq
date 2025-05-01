package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.exception.ExorcistaSinAngelesException;
import ar.edu.unq.epersgeist.modelo.exception.InvalidFieldsException;
import ar.edu.unq.epersgeist.modelo.exception.NullRequiredFieldsException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor @EqualsAndHashCode
@ToString

@Entity
public class Medium implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    @Check(constraints = "mana_max > 0")
    private Integer manaMax;

    @Column(nullable = false)
    @Check(constraints = "mana BETWEEN 0 AND mana_max")
    private Integer mana;

    @OneToMany(mappedBy = "medium", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Angel> angeles = new ArrayList<>();

    @OneToMany(mappedBy = "medium", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Demonio> demonios = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Ubicacion ubicacion;


    public Medium(String nombre, Integer manaMax, Integer mana, Ubicacion ubicacion) {
        this.validarLosCampos(nombre,manaMax,mana);
        this.nombre = nombre;
        this.manaMax = manaMax;
        this.mana = mana;
        this.ubicacion = ubicacion;
    }

    private void validarLosCampos(String nombre, Integer manaMax, Integer mana)
            throws InvalidFieldsException, NullRequiredFieldsException {

        if (nombre == null || manaMax == null || mana == null )
            throw new NullRequiredFieldsException(this.getClass().getSimpleName());
        else if (mana < 0 || mana > manaMax)
            throw new InvalidFieldsException(this.getClass().getSimpleName());
    }

    public void realizarConexion(Espiritu espiritu){

       espiritu.agregarAEspiritusDeMedium(this);

    }

    public void descansar(){
        this.aumentarMana(15);
        this.recuperarConexion(angeles);
        this.recuperarConexion(demonios);
    }

    private void recuperarConexion(List<? extends Espiritu> espiritus){
        for(Espiritu espiritu : espiritus){
            espiritu.aumentarNivelDeConexionEn(5);
        }
    }

    private void aumentarMana(int i) {
        int manaActual = getMana();
        setMana(i + manaActual);
    }

    public void setMana(int i) {
        if (i < 0) {
            this.mana = 0;
        } else if (i > manaMax) {
            this.mana = manaMax;
        } else {
            this.mana = i;
        }

    }
    public void exorcizarA(Medium medium){

        this.verificarSiPuedeExorcizar();

        for (Angel angel : angeles){

            List<Demonio> otrosDemonios = medium.getDemonios();

            if(medium.poseido()){

                angel.atacar(otrosDemonios.get(0));

            }else{
                break;
            }
        }
    }

    private void verificarSiPuedeExorcizar(){
        if (this.angeles.isEmpty()){
            throw new ExorcistaSinAngelesException(this);
        }
    }

    public void desvincularseDeEspiritu(Espiritu espiritu){

        espiritu.desconectarDeMedium(this);
    }

    private boolean poseido(){
        return (!demonios.isEmpty());
    }

    public void invocar(Espiritu espiritu) {

        verificarSiEstaLibre(espiritu);
        if (this.mana >= 10) {
            espiritu.setUbicacion(this.ubicacion);
            this.mana -= 10;
        }
    }

    private void verificarSiEstaLibre(Espiritu espiritu) {
        if(!espiritu.estaLibre()){
            throw new RuntimeException("El espiritu no esta libre");
        }
    }

    public void setUbicacion(Ubicacion unaUbicacion) {
        this.ubicacion = unaUbicacion;
        this.angeles.forEach(angel -> angel.moverConMediumHacia(this, unaUbicacion));
        this.demonios.forEach(demonio -> demonio.moverConMediumHacia(this, unaUbicacion));
    }

    public void agregarAAngeles(Angel angel) {
        this.getAngeles().add(angel);
    }

    public void agregarADemonios(Demonio demonio) {
        this.getDemonios().add(demonio);
    }

    public void quitarDeAngeles(Angel angel) {
        this.getAngeles().remove(angel);
    }

    public void quitarDeDemonios(Demonio demonio) {
        this.getDemonios().remove(demonio);
    }
}