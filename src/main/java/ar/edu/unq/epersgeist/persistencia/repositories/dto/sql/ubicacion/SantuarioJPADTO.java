package ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.ubicacion;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "santuario")
@EntityListeners(AuditingEntityListener.class)

@Where(clause = "deleted is null")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class SantuarioJPADTO extends UbicacionJPADTO {

    public SantuarioJPADTO(Ubicacion ubicacion) {
        super(ubicacion);
    }

    public Ubicacion aModelo() {
        Ubicacion ubicacion = new Santuario(this.nombre, this.energia);
        ubicacion.setId(this.id);
        return ubicacion;
    }

    public Ubicacion aModelo(Ubicacion ubicacion) {
        ubicacion.setId(this.id);
        return ubicacion;

    }

}


