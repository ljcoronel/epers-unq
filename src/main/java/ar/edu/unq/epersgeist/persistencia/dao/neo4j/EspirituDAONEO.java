package ar.edu.unq.epersgeist.persistencia.dao.neo4j;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.espiritu.EspirituNEODTO;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EspirituDAONEO extends Neo4jRepository<EspirituNEODTO, Long> {
    @Query("""
        MATCH (u:UBICACION:SANTUARIO)
        OPTIONAL MATCH (e:ESPIRITU)-[:TIENE_UBICACION]->(u)
        WITH u, SUM(CASE WHEN e:DEMONIO THEN 1 ELSE 0 END) AS cantidadDeDemonios, SUM(CASE WHEN e:ANGEL THEN 1 ELSE 0 END) AS cantidadDeAngeles
        ORDER BY cantidadDeDemonios - cantidadDeAngeles DESC
        LIMIT 1
        RETURN u.id
    """)
    Long santuarioCorrupto();
}
