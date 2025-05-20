package ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.ubicacion;

import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO.TipoUbicacion;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.ubicacion.UbicacionNEODTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.Date;

@Entity(name = "ubicacion")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@EntityListeners(AuditingEntityListener.class)
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "nombre", "deleted" }) })
@SQLDelete(sql = "update ubicacion set deleted = current_timestamp where id =?")
@Where(clause = "deleted is null")

@Getter
@Setter
@NoArgsConstructor
@ToString
public abstract class UbicacionJPADTO {
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id;

        @Column(nullable = false)
        @Property(name="nombre")
        String nombre;

        @Column(nullable = false)
        int energia;

        Date deleted;

        @CreatedDate
        @Column(updatable = false)
        Date createdAt;

        @LastModifiedDate
        Date updatedAt;


    public UbicacionJPADTO(Ubicacion ubicacion) {
        id = ubicacion.getId();
        nombre = ubicacion.getNombre();
        energia = ubicacion.getEnergia();
    }

    public static UbicacionJPADTO desdeModelo(Ubicacion ubicacion) {
        if (ubicacion == null) return null;
        TipoUbicacion tipo = TipoUbicacion.obtenerTipoUbicacion(ubicacion);
        return tipo.getDto(ubicacion);
    }

    public abstract Ubicacion aModelo();

    public abstract Ubicacion aModelo(Ubicacion ubicacion);

    public Ubicacion aModelo(UbicacionNEODTO ubicacionNEODTO) {
        Ubicacion ubicacion = this.aModelo();
        return ubicacionNEODTO.aModelo(ubicacion);
    }
}


