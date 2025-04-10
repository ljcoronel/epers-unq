package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.io.Serializable;
import java.nio.channels.AlreadyConnectedException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Check(constraints = "manaMax > 0")
    private Integer manaMax;
    @Column(nullable = false)
    @Check(constraints = "mana BETWEEN 0 AND manaMax")
    private Integer mana;
    @OneToMany(mappedBy = "medium", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Angel> angeles = new ArrayList<>();
    @OneToMany(mappedBy = "medium", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Demonio> demonios = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    private Ubicacion ubicacion;


    public Medium(String nombre, Integer manaMax, Integer mana) {
        this.nombre = nombre;
        this.manaMax = manaMax;
        this.mana = mana;
    }

    public void conectarseAEspiritu(Espiritu espiritu) {

        if(espiritu.getMedium() != null)return;

        this.realizarConexion(espiritu);
        espiritu.conectarse();
    }


    private void realizarConexion(Espiritu espiritu){

        espiritu.setMedium(this);

        if (espiritu instanceof Angel) {
            angeles.add((Angel) espiritu);
        }else{
            demonios.add((Demonio) espiritu);
        }

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
    public void agregarAngel(Angel angel){
        angeles.add(angel);
    }


}