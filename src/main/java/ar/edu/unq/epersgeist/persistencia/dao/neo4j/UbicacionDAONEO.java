package ar.edu.unq.epersgeist.persistencia.dao.neo4j;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.UbicacionNEODTO;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UbicacionDAONEO extends Neo4jRepository<UbicacionNEODTO, Long> {

   @Query("MATCH (a:UBICACION {id: $unaId}) " +
           "SET a.energia= $cantEnergia, a.nombre = $unNombre")
   void actualizar(@Param("unaId") Long id, @Param("unNombre") String nombre, @Param("cantEnergia") int energia);

   @Query("MATCH (origen:UBICACION {id: $idOrigen})   MATCH (destino:UBICACION {id: $idDestino})   RETURN exists( (origen) -[:CONECTADA*1]-> (destino) )")
   boolean estanConectadas(@Param("idOrigen") Long idOrigen, @Param("idDestino") Long idDestino);

    @Query("MATCH (origen:UBICACION {id: $idOrigen}), (destino:UBICACION {id: $idDestino}), p = shortestPath((origen)-[*]-(destino)) WHERE $idOrigen <> $idDestino WITH nodes(p)[1..] AS nodos UNWIND nodos AS n RETURN n")
    List<UbicacionNEODTO> caminoMasCorto(@Param("idOrigen") Long idOrigen, @Param("idDestino") Long idDestino);
//no incluye al nodo origen

    @Query("MATCH (origen:UBICACION {id: $idOrigen}), (destino:UBICACION {id: $idDestino}) " +
        "RETURN CASE WHEN $idOrigen = $idDestino THEN true " +
        "            ELSE exists( (origen)-[:CONECTADA*1..]->(destino) ) END")
    boolean estanConectadasDeAlgunModo(@Param("idOrigen") Long idOrigen, @Param("idDestino") Long idDestino);


    @Query("MATCH (u:UBICACION) WHERE u.energia > $umbralDeEnergia RETURN u")
    List<UbicacionNEODTO> ubicacionesConEnergiaSueriorAUmbral(int umbralDeEnergia);
}
