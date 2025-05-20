package ar.edu.unq.epersgeist.persistencia.dao.neo4j;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.MediumNEODTO;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MediumDAONEO extends Neo4jRepository<MediumNEODTO, Long> {

    @Query("""
        MATCH (m:MEDIUM)-[:TIENE_UBICACION]->(u:UBICACION:SANTUARIO {id: $unaId})
        OPTIONAL MATCH (d:ESPIRITU:DEMONIO)-[:CONECTADO_CON]->(m)
        WITH m, COUNT(d) AS cantidadDeDemonios
        ORDER BY cantidadDeDemonios DESC
        LIMIT 1
        RETURN m.id
    """)
    Long mediumConMasDemonios(@Param("unaId") Long id);
}
