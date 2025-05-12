package ar.edu.unq.epersgeist.persistencia.repositories.DTO;

import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
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
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.Date;

//@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@EntityListeners(AuditingEntityListener.class)
//@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "nombre", "deleted" }) })
//@SQLDelete(sql = "update ubicacion set deleted = current_timestamp where id =?")
//@Where(clause = "deleted is null")
@Node()
public record UbicacionRepositoryDTO(
//        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        @org.springframework.data.neo4j.core.schema.Id
        Long id,

//        @Column(nullable = false)
        @Property(name="nombre")
        String nombre,

//        @Column(nullable = false)
        int energia,

        Date deleted,

//        @CreatedDate
//        @Column(updatable = false)
        Date createdAt,

//        @LastModifiedDate
        Date updatedAt) {

    public static UbicacionRepositoryDTO desdeModelo(Ubicacion ubicacion) {
        return new UbicacionRepositoryDTO(ubicacion.getId(), ubicacion.getNombre(), ubicacion.getEnergia(), ubicacion.getDeleted(), ubicacion.getCreatedAt(), ubicacion.getUpdatedAt());
    }

    public Ubicacion aModelo() {
        return null;
    }
}


