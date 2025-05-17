package ar.edu.unq.epersgeist.persistencia.dao.neo4j;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.UbicacionNEODTO;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface UbicacionDAONEO extends Neo4jRepository<UbicacionNEODTO, Long> {
}
