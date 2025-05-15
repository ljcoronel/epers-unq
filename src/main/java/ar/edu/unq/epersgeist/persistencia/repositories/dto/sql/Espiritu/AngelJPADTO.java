package ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.Espiritu;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.MediumJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.UbicacionJPADTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString

//@Entity(name = "angel")
//@EntityListeners(AuditingEntityListener.class)
//@Where(clause = "deleted is null")
public class AngelJPADTO extends EspirituJPADTO {

    public AngelJPADTO(Espiritu espiritu) {
        super(espiritu);
    }

    public AngelJPADTO(Espiritu espiritu, MediumJPADTO mediumDTO) {
        super(espiritu, mediumDTO);
    }

    public Espiritu aModelo() {
        Espiritu espiritu = new Angel(this.getNivelDeConexion(), this.getNombre(), this.getUbicacion().aModelo());
        espiritu.setId(this.getId());
        espiritu.setMedium(this.getMedium() != null ? this.getMedium().aModelo(): null);
        return espiritu;
    }

    public Espiritu aModelo(Medium medium) {
        Espiritu espiritu = new Angel(this.getNivelDeConexion(), this.getNombre(), this.getUbicacion().aModelo());
        espiritu.setId(this.getId());
        espiritu.setMedium(medium);
        return espiritu;
    }

    public Espiritu aModelo(Espiritu espiritu) {
        espiritu.setId(this.getId());
        return espiritu;
    }
}
