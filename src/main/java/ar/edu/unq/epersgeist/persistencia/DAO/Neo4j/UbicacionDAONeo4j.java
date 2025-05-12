package ar.edu.unq.epersgeist.persistencia.DAO.Neo4j;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.repositories.DTO.UbicacionRepositoryDTO;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface UbicacionDAONeo4j extends Neo4jRepository<UbicacionRepositoryDTO, Long> {
}
