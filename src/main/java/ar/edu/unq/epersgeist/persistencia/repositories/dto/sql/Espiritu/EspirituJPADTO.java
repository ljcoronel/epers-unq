package ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.Espiritu;

import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.MediumJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.UbicacionJPADTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity(name = "espiritu")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@EntityListeners(AuditingEntityListener.class)
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "nombre", "deleted" }) })
@SQLDelete(sql = "update espiritu set deleted = current_timestamp  where id =?")
@Where(clause = "deleted is null")
public class EspirituJPADTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Check(name = "valoresValidosDeConexion", constraints = "nivel_de_conexion BETWEEN 0 AND 100")
    private Integer nivelDeConexion;

    @Column(length = 50, nullable = false)
    private String nombre;

    @ManyToOne
    private MediumJPADTO medium;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private UbicacionJPADTO ubicacion;

    private Date deleted;

    @CreatedDate
    @Column(updatable = false)
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    @Column(updatable = false)
    private TipoEspiritu tipo;


    public EspirituJPADTO (Espiritu espiritu) {
        id = espiritu.getId();
        nivelDeConexion = espiritu.getNivelDeConexion();
        nombre = espiritu.getNombre();
        medium = MediumJPADTO.desdeModelo(espiritu.getMedium());
        ubicacion = UbicacionJPADTO.desdeModelo(espiritu.getUbicacion());
        tipo = TipoEspiritu.getValueOfEspiritu(espiritu);
    }


    public EspirituJPADTO(Espiritu espiritu, MediumJPADTO mediumDTO) {
        id = espiritu.getId();
        nivelDeConexion = espiritu.getNivelDeConexion();
        nombre = espiritu.getNombre();
        medium = mediumDTO;
        ubicacion = UbicacionJPADTO.desdeModelo(espiritu.getUbicacion());
        tipo = TipoEspiritu.getValueOfEspiritu(espiritu);
    }

//    public static EspirituJPADTO desdeModelo(Espiritu espiritu) {
//        TipoEspiritu tipo = TipoEspiritu.getValueOfEspiritu(espiritu);
//        return tipo.getDto(espiritu);
//    }
//
//    public Espiritu aModelo(Espiritu espiritu) {
//    }

    public Espiritu aModelo() {
        Espiritu espiritu = this.tipo.getEspirituInstance(this.nivelDeConexion, this.nombre, this.ubicacion.aModelo());
        espiritu.setId(this.id);
        espiritu.setMedium(this.medium != null ? this.medium.aModelo(): null);
        return espiritu;
    }

    public Espiritu aModelo(Medium medium) {
        Espiritu espiritu = this.tipo.getEspirituInstance(this.nivelDeConexion, this.nombre, this.ubicacion.aModelo());
        espiritu.setId(this.id);
        espiritu.setMedium(medium);
        return espiritu;
    }

    public Espiritu aModelo(Espiritu espiritu) {
        espiritu.setId(this.id);
        return espiritu;
    }
}
