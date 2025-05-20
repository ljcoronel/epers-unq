package ar.edu.unq.epersgeist.persistencia.dao.neo4j;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.ubicacion.UbicacionNEODTO;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UbicacionDAONEO extends Neo4jRepository<UbicacionNEODTO, Long> {

   @Query("MATCH (a:UBICACION {id: $unaId}) " +
           "SET a.energia= $cantEnergia, a.nombre = $unNombre")
    void actualizar(@Param("unaId") Long id, @Param("unNombre") String nombre, @Param("cantEnergia") int energia);

    @Query("MATCH (origen:UBICACION {id: $idOrigen})   MATCH (destino:UBICACION {id: $idDestino})   RETURN exists( (origen) -[:CONECTADA*1]-> (destino) )")
    boolean estanConectadas(@Param("idOrigen") Long idOrigen, @Param("idDestino") Long idDestino);
}
