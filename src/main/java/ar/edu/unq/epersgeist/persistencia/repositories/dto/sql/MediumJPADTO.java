package ar.edu.unq.epersgeist.persistencia.repositories.dto.sql;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity(name = "medium")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "update medium set deleted = current_timestamp where id =?")
@Where(clause = "deleted is null")
public class MediumJPADTO {

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

    @OneToMany(mappedBy = "medium", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EspirituJPADTO> angeles = new ArrayList<>();

    @OneToMany(mappedBy = "medium", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EspirituJPADTO> demonios = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER) // usamos eager ya que solo es una ubicación, que tiene atributos simples (y no más objetos dentro)
    private UbicacionJPADTO ubicacion;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    private Date deleted;

    public static MediumJPADTO desdeModelo(Medium medium) {
        MediumJPADTO dto = new MediumJPADTO();
        dto.id = medium.getId();
        dto.nombre = medium.getNombre();
        dto.manaMax = medium.getManaMax();
        dto.mana = medium.getMana();
        dto.ubicacion = UbicacionJPADTO.desdeModelo(medium.getUbicacion());
        dto.angeles = medium.getAngeles().stream()
                .map(EspirituJPADTO::desdeModelo)
                .collect(Collectors.toList());
        dto.demonios = medium.getDemonios().stream()
                .map(EspirituJPADTO::desdeModelo)
                .collect(Collectors.toList());
        return dto;
    }

    public Medium aModelo() {
        Medium medium = new Medium(this.nombre, this.manaMax, this.mana, this.ubicacion.aModelo());
        medium.setId(this.id);
        medium.setAngeles(this.angeles.stream()
                .map(dto -> (Angel) dto.aModelo())
                .collect(Collectors.toList()));
        medium.setDemonios(this.demonios.stream()
                .map(dto -> (Demonio) dto.aModelo())
                .collect(Collectors.toList()));
        return medium;
    }
}
