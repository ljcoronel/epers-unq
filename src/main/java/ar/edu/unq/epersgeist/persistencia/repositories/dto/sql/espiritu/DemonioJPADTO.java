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

    public Espiritu aModelo() {
        Espiritu espiritu = new Demonio(this.getNivelDeConexion(), this.getNombre(), this.getUbicacion().aModelo());
        espiritu.setId(this.getId());
        espiritu.setMedium(this.getMedium() != null ? this.getMedium().aModelo(): null);
        return espiritu;
    }

    public Espiritu aModelo(Medium medium) {
        Espiritu espiritu = new Demonio(this.getNivelDeConexion(), this.getNombre(), this.getUbicacion().aModelo());
        espiritu.setId(this.getId()); // TODO no repetir esto por subclase
        espiritu.setMedium(medium);
        return espiritu;
    }

    public Espiritu aModelo(Espiritu espiritu) {
        espiritu.setId(this.getId());
        return espiritu;
    }

    @Override
    public Espiritu aModelo(Medium medium, Espiritu espiritu) {
        espiritu.setId(this.getId()); // TODO no repetir esto por subclase
        espiritu.setMedium(medium);
        return espiritu;
    }
}
