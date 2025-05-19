package ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.ubicacion;

import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "cementerio")
@EntityListeners(AuditingEntityListener.class)

@Where(clause = "deleted is null")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CementerioJPADTO extends UbicacionJPADTO {

    public CementerioJPADTO(Ubicacion ubicacion) {
        super(ubicacion);
    }

    public Ubicacion aModelo() {
        Ubicacion ubicacion = new Cementerio(this.nombre, this.energia);
        ubicacion.setId(this.id);
        return ubicacion;
    }

    public Ubicacion aModelo(Ubicacion ubicacion) {
        ubicacion.setId(this.id);
        return ubicacion;

    }

}


