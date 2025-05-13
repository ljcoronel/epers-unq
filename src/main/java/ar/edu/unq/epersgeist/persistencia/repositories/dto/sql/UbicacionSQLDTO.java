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

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@EntityListeners(AuditingEntityListener.class)
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "nombre", "deleted" }) })
@SQLDelete(sql = "update ubicacion set deleted = current_timestamp where id =?")
@Where(clause = "deleted is null")
@Setter
@Getter
public class UbicacionSQLDTO{
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



    public UbicacionSQLDTO(TipoUbicacion tipoUbicacion, Long id, String nombre, int energia, Date deleted, Date createdAt, Date updatedAt) {
        this.tipoUbicacion = tipoUbicacion;
        this.id = id;
        this.nombre = nombre;
        this.energia = energia;
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

    }

    public static UbicacionSQLDTO desdeModelo(Ubicacion ubicacion) {
        return new UbicacionSQLDTO(
                TipoUbicacion.obtenerTipoUbicacion(ubicacion),
                ubicacion.getId(),
                ubicacion.getNombre(),
                ubicacion.getEnergia(),
                ubicacion.getDeleted(),
                ubicacion.getCreatedAt(),
                ubicacion.getUpdatedAt());
    }

    public Ubicacion aModelo() {
        Ubicacion ubicacion = this.tipoUbicacion.getUbicacionInstance(this.nombre, this.energia);
        ubicacion.setId(this.id);
        ubicacion.setDeleted(this.deleted);
        ubicacion.setCreatedAt(this.createdAt);
        ubicacion.setUpdatedAt(this.updatedAt);
        return ubicacion;

    }

    public Ubicacion aModelo(Ubicacion ubicacion) {
        ubicacion.setId(this.id);
        ubicacion.setDeleted(this.deleted);
        ubicacion.setCreatedAt(this.createdAt);
        ubicacion.setUpdatedAt(this.updatedAt);
        return ubicacion;

    }

}


