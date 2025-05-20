package ar.edu.unq.epersgeist.persistencia.dao.neo4j;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.espiritu.EspirituNEODTO;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface MediumDAONEO extends Neo4jRepository<EspirituNEODTO, Long> {

    @Query("""
            MATCH (d:ESPIRITU:DEMONIO)-[:CONECTADO_CON]->(m:MEDIUM)-[:TIENE_UBICACION]->(u:UBICACION {id: $unaId})
            WITH m, COUNT(d) AS cantidadDeDemonios
            ORDER BY cantidadDeDemonios DESC
            LIMIT 1
            RETURN m.id
           """)
    Long mediumConMasDemonios(Long id);
}
