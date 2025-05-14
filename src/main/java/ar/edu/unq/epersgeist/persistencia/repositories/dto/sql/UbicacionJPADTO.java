package ar.edu.unq.epersgeist.persistencia.repositories.dto.sql;

import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO.TipoUbicacion;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
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
@Setter
@Getter
public class UbicacionJPADTO {
        @Column(updatable = false)
        TipoUbicacion tipoUbicacion;

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

    public static UbicacionJPADTO desdeModelo(Ubicacion ubicacion) {
        UbicacionJPADTO dto = new UbicacionJPADTO();
        dto.tipoUbicacion = TipoUbicacion.obtenerTipoUbicacion(ubicacion);
        dto.id = ubicacion.getId();
        dto.nombre = ubicacion.getNombre();
        dto.energia = ubicacion.getEnergia();
        return dto;
    }

    public Ubicacion aModelo() {
        Ubicacion ubicacion = this.tipoUbicacion.getUbicacionInstance(this.nombre, this.energia);
        ubicacion.setId(this.id);
        return ubicacion;
    }

    public Ubicacion aModelo(Ubicacion ubicacion) {
        ubicacion.setId(this.id);
        return ubicacion;

    }

}


