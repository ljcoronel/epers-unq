package ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.espiritu;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
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

@Entity(name = "angel")
@EntityListeners(AuditingEntityListener.class)
@Where(clause = "deleted is null")
public class AngelJPADTO extends EspirituJPADTO {

    public AngelJPADTO(Espiritu espiritu) {
        super(espiritu);
    }

    public AngelJPADTO(Espiritu espiritu, MediumJPADTO mediumDTO) {
        super(espiritu, mediumDTO);
    }

    @Override
    public Espiritu instanciarEspiritu() {
        return new Angel(this.getNivelDeConexion(), this.getNombre(), this.getUbicacion().aModelo());
    }

}
