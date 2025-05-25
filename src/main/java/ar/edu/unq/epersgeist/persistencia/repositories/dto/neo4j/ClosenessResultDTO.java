package ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ClosenessResultDTO {
    private UbicacionNEODTO origen;
    private Double closeness;
}
