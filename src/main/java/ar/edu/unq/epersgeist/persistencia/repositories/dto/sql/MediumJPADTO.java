package ar.edu.unq.epersgeist.persistencia.repositories.dto.sql;

import ar.edu.unq.epersgeist.modelo.Coordenada;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.espiritu.AngelJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.espiritu.DemonioJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.ubicacion.UbicacionJPADTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.geo.Point;
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

    @OrderBy("id ASC")
    @OneToMany(mappedBy = "medium", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AngelJPADTO> angeles = new ArrayList<>();

    @OrderBy("id ASC")
    @OneToMany(mappedBy = "medium", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DemonioJPADTO> demonios = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER) // usamos eager ya que solo es una ubicación, que tiene atributos simples (y no más objetos dentro)
    private UbicacionJPADTO ubicacion;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    private Date deleted;

    public static MediumJPADTO desdeModelo(Medium medium) {
        if (medium == null) return null;
        MediumJPADTO dto = new MediumJPADTO();
        dto.id = medium.getId();
        dto.nombre = medium.getNombre();
        dto.manaMax = medium.getManaMax();
        dto.mana = medium.getMana();
        dto.ubicacion = UbicacionJPADTO.desdeModelo(medium.getUbicacion());
        dto.angeles = medium.getAngeles().stream()
                .map(angel -> new AngelJPADTO(angel, dto))
                .collect(Collectors.toList()).reversed();
        dto.demonios = medium.getDemonios().stream()
                .map(demonio -> new DemonioJPADTO(demonio, dto))
                .collect(Collectors.toList());
        return dto;
    }

    public Medium aModelo(Point coordenada) {
        Coordenada nuevaCoordenada = new Coordenada(coordenada.getX(), coordenada.getY());
        Ubicacion ubicacionParaTodos = this.ubicacion.aModelo();
        Medium medium = new Medium(this.nombre, this.manaMax, this.mana, ubicacionParaTodos, nuevaCoordenada);
        medium.setId(this.id);
        medium.setAngeles(this.angeles.stream()
                .map(dto -> (Angel) dto.aModelo(medium, ubicacionParaTodos))
                .collect(Collectors.toList()).reversed());
        medium.setDemonios(this.demonios.stream()
                .map(dto -> (Demonio) dto.aModelo(medium, ubicacionParaTodos))
                .collect(Collectors.toList()));
        return medium;
    }

    public Medium aModelo() {
        Ubicacion ubicacionParaTodos = this.ubicacion.aModelo();
        Medium medium = new Medium(this.nombre, this.manaMax, this.mana, ubicacionParaTodos, null);
        medium.setId(this.id);
        medium.setAngeles(this.angeles.stream()
                .map(dto -> (Angel) dto.aModelo(medium, ubicacionParaTodos))
                .collect(Collectors.toList()));
        medium.setDemonios(this.demonios.stream()
                .map(dto -> (Demonio) dto.aModelo(medium, ubicacionParaTodos))
                .collect(Collectors.toList()));
        return medium;
    }

    public Medium aModelo(Medium medium) {
        medium.setId(this.id);
        return medium;
    }
}
