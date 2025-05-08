package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.exception.ExorcistaSinAngelesException;
import ar.edu.unq.epersgeist.modelo.exception.InvalidFieldsException;
import ar.edu.unq.epersgeist.modelo.exception.NullRequiredFieldsException;
import ar.edu.unq.epersgeist.modelo.exception.EntidadInexistenteException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        getUbicacion().recuperarManaParaMedium(this);
        getUbicacion().recuperarConexionParaAngeles(getAngeles());
        getUbicacion().recuperarConexionParaDemonios(getDemonios());
    }

    public void aumentarMana(int cantidad) {
        int mana = this.getMana();
        setMana(mana + cantidad);
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
    public void exorcizarA(Optional<Medium> optionalMedium){

        if (optionalMedium.isEmpty()) throw new EntidadInexistenteException("el Medium a exorcizar no existe");
        Medium medium = optionalMedium.get();

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

    public void invocar(Optional<Espiritu> opcionalEspiritu) {

        if(opcionalEspiritu.isEmpty()) throw new EntidadInexistenteException("el espiritu a invocar no existe");

        Espiritu espiritu = opcionalEspiritu.get();

        verificarSiEstaLibre(espiritu);

        if (this.getMana() >= 10) {
            this.getUbicacion().invocarAEspirituParaMedium(espiritu, this);
        }
    }

    private void verificarSiEstaLibre(Espiritu espiritu) {
        if(!espiritu.estaLibre()){
            throw new RuntimeException("El espiritu no esta libre");
        }
    }

    public void setUbicacion(Ubicacion unaUbicacion) {
        this.ubicacion = unaUbicacion;
        List<Angel> angeles=  new ArrayList<>(this.getAngeles());
        List<Demonio> demonios = new ArrayList<>(this.getDemonios());

        angeles.forEach(angel -> angel.moverConMediumHacia(this, unaUbicacion));
        demonios.forEach(demonio -> demonio.moverConMediumHacia(this, unaUbicacion));
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

    public void usarManaParaInvocar() {
        this.setMana(this.getMana() - 10);
    }
}