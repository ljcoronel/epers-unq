package ar.edu.unq.epersgeist.persistencia.repositories.dto.mongo;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.neo4j.core.schema.Node;

@Getter @Setter

@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Document("Angel")
public class AngelMongoDTO extends EspirituMongoDTO{

    public AngelMongoDTO(Espiritu espiritu){super(espiritu);
    }
}
