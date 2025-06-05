package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.exception.EspirituLibreException;
import ar.edu.unq.epersgeist.modelo.exception.ExorcistaSinAngelesException;
import ar.edu.unq.epersgeist.modelo.exception.EntidadInexistenteException;
import ar.edu.unq.epersgeist.modelo.exception.MediumEnDistintaUbicacionException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import lombok.*;
import org.springframework.data.geo.Point;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter @Setter @NoArgsConstructor @EqualsAndHashCode @ToString
public class Medium implements Serializable {

    private Long id;
    private String nombre;
    private Integer manaMax;
    private Integer mana;
    private List<Angel> angeles;
    private List<Demonio> demonios;
    private Ubicacion ubicacion;
    private Point coordenada;

    public Medium(String nombre, Integer manaMax, Integer mana, Ubicacion ubicacion, Coordenada coordenada) {
        this.nombre = nombre;
        this.manaMax = manaMax;
        this.mana = mana;
        this.angeles = new ArrayList<>();
        this.demonios = new ArrayList<>();
        this.ubicacion = ubicacion;
        this.coordenada = coordenada;
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

        Medium medium = optionalMedium.orElseThrow(() -> new EntidadInexistenteException("el Medium a exorcizar no existe"));

        this.verificarSiPuedeExorcizarAMedium(medium);

        for (Angel angel : angeles){

            List<Demonio> otrosDemonios = medium.getDemonios();

            if(medium.poseido()){

                angel.atacar(otrosDemonios.get(0));

            }else{
                break;
            }
        }
    }

    private void verificarSiPuedeExorcizarAMedium(Medium medium){
        if (this.angeles.isEmpty()){
            throw new ExorcistaSinAngelesException(this);
        }
        if (!(medium.getUbicacion().getId().equals(this.ubicacion.getId())))
            throw new MediumEnDistintaUbicacionException("Los médiums están en ubicaciones distintas");
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
            throw new EspirituLibreException(espiritu);
        }
    }

    public void mover(Optional<Ubicacion> ubicacionOptional) {
        Ubicacion unaUbicacion = ubicacionOptional.orElseThrow(() -> new EntidadInexistenteException("la ubicacion a mover no existe"));
        this.setUbicacion(unaUbicacion);
        List<Angel> angeles=  new ArrayList<>(this.getAngeles());
        List<Demonio> demonios = new ArrayList<>(this.getDemonios());

        angeles.forEach(angel -> angel.moverConMediumHacia(unaUbicacion));
        demonios.forEach(demonio -> demonio.moverConMediumHacia(unaUbicacion));
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

    public Long getIdDeUbicacion() {
        return this.ubicacion.getId();
    }
}