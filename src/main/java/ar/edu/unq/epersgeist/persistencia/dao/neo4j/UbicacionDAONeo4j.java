package ar.edu.unq.epersgeist.persistencia.dao.neo4j;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.UbicacionNeo4jDTO;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface UbicacionDAONeo4j extends Neo4jRepository<UbicacionNeo4jDTO, Long> {
}
