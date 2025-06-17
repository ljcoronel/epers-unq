package ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.espiritu;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.MediumJPADTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@ToString

@Entity(name = "demonio")
@EntityListeners(AuditingEntityListener.class)
@Where(clause = "deleted is null")
public class DemonioJPADTO extends EspirituJPADTO {

    public DemonioJPADTO(Espiritu espiritu) {
        super(espiritu);
    }

    public DemonioJPADTO(Espiritu espiritu, MediumJPADTO mediumDTO) {
        super(espiritu, mediumDTO);
    }

    @Override
    public Espiritu instanciarEspiritu() {
        return new Demonio(this.getNivelDeConexion(), this.getNombre());
    }


}
