package ar.edu.unq.epersgeist.modelo.ubicacion;

import ar.edu.unq.epersgeist.modelo.EfectosDeUbicacion;
import ar.edu.unq.epersgeist.modelo.UbicacionesRecibidoras;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;

@Getter @Setter @NoArgsConstructor @ToString

@Entity
// singletable, por el momento tiene sentido porque ambos tipos de ubicación solo cambian en comportamiento, se mantiene la misma estructura.
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Ubicacion,  UbicacionesRecibidoras  {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private int energia;

    public Ubicacion(String nombre, int energia) {

        this.nombre = nombre;
        this.setEnergia(energia);
    }

    public void setEnergia(int i) {
        if (i < 0) {
            this.energia = 0;
        } else if (i > 100) {
            this.energia = 100;
        } else {
            this.energia = i;
        }

    @Override
    public void recibir(EfectosDeUbicacion espiritu) {
        throw new NotImplementedException("recibir no implementado");
    }

    public abstract void invocarAEspirituParaMedium(Espiritu espiritu, Medium medium);

    public abstract void recuperarConexionParaAngeles(List<Angel> angeles);

    public abstract void recuperarConexionParaDemonios(List<Demonio> demonios);

    public abstract void recuperarManaParaMedium(Medium medium);
}
