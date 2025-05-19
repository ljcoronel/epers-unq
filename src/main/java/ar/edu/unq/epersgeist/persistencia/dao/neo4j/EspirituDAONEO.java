package ar.edu.unq.epersgeist.persistencia.dao.neo4j;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.UbicacionNEODTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.espiritu.EspirituNEODTO;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EspirituDAONEO extends Neo4jRepository<EspirituNEODTO, Long> {
    @Query("MATCH (a:ESPIRITU {id: $unaId}) " +
            "SET a.nombre = $unNombre")
    void actualizar(@Param("unaId") Long id, @Param("unNombre") String nombre);
}
