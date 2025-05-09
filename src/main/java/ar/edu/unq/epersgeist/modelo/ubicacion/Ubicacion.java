package ar.edu.unq.epersgeist.modelo.ubicacion;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;

@Getter @Setter @NoArgsConstructor @ToString

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@EntityListeners(AuditingEntityListener.class)
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "nombre", "deleted" }) })
@SQLDelete(sql = "update ubicacion set deleted = current_timestamp where id =?")
@Where(clause = "deleted = null")
public abstract class Ubicacion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private int energia;

    private Date deleted;

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

    }

    public abstract void invocarAEspirituParaMedium(Espiritu espiritu, Medium medium);

    public abstract void recuperarConexionParaAngeles(List<Angel> angeles);

    public abstract void recuperarConexionParaDemonios(List<Demonio> demonios);

    public abstract void recuperarManaParaMedium(Medium medium);
}
