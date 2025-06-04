package ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.espiritu;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter

@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Document("Angel")
public class AngelMongoDTO extends EspirituMongoDTO{

    public AngelMongoDTO(Espiritu espiritu){super(espiritu);
    }
}
