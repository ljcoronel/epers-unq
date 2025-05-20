package ar.edu.unq.epersgeist.persistencia.dao.neo4j;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.MediumNEODTO;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediumDAONEO extends Neo4jRepository<MediumNEODTO, Long> {
}
