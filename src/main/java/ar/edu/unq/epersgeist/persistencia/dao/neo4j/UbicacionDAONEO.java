package ar.edu.unq.epersgeist.persistencia.dao.neo4j;

import ar.edu.unq.epersgeist.persistencia.ClosenessResult;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.ubicacion.UbicacionNEODTO;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UbicacionDAONEO extends Neo4jRepository<UbicacionNEODTO, Long> {

    @Query("""
        MATCH (u:UBICACION:SANTUARIO)
        OPTIONAL MATCH (e:ESPIRITU)-[:TIENE_UBICACION]->(u)
        WITH u, SUM(CASE WHEN e:DEMONIO THEN 1 ELSE 0 END) AS cantidadDeDemonios, SUM(CASE WHEN e:ANGEL THEN 1 ELSE 0 END) AS cantidadDeAngeles
        ORDER BY cantidadDeDemonios - cantidadDeAngeles DESC
        LIMIT 1
        RETURN u
    """)
    UbicacionNEODTO santuarioCorrupto();

   @Query("MATCH (a:UBICACION {id: $unaId}) " +
           "SET a.energia= $cantEnergia, a.nombre = $unNombre")
   void actualizar(@Param("unaId") Long id, @Param("unNombre") String nombre, @Param("cantEnergia") int energia);

   @Query("MATCH (origen:UBICACION {id: $idOrigen})   MATCH (destino:UBICACION {id: $idDestino})   RETURN exists( (origen) -[:CONECTADA*1]-> (destino) )")
   boolean estanConectadas(@Param("idOrigen") Long idOrigen, @Param("idDestino") Long idDestino);

   @Query("MATCH (origen:UBICACION {id: $idOrigen}), (destino:UBICACION {id: $idDestino}), p = shortestPath((origen)-[*]-(destino)) WHERE $idOrigen <> $idDestino WITH nodes(p)[1..] AS nodos UNWIND nodos AS n RETURN n.id")
    List<Long> caminoMasCortoIds(@Param("idOrigen") Long idOrigen, @Param("idDestino") Long idDestino);//no incluye al nodo origen

/*
   @Query("MATCH (origen:UBICACION {id: $idOrigen}), " +
         "      (destino:UBICACION {id: $idDestino}), " +
         "      p = shortestPath((origen)-[*]-(destino)) " +
         "UNWIND nodes(p) AS n " +
         "RETURN n.id")
   List<Long> caminoMasCortoIds(@Param("idOrigen") Long idOrigen, @Param("idDestino") Long idDestino);//incluye al nodo origen
*/

    @Query("MATCH (origen:UBICACION {id: $idOrigen}), (destino:UBICACION {id: $idDestino}) " +
        "RETURN CASE WHEN $idOrigen = $idDestino THEN true " +
        "            ELSE exists( (origen)-[:CONECTADA*1..]->(destino) ) END")
    boolean estanConectadasDeAlgunModo(@Param("idOrigen") Long idOrigen, @Param("idDestino") Long idDestino);

    @Query("""
        UNWIND $ids AS id
        MATCH (u:UBICACION {id: id})
        WITH COLLECT(u) AS ubicaciones
        UNWIND ubicaciones AS origen
        UNWIND ubicaciones AS destino
        WHERE origen.id <> destino.id
        WITH origen, destino
        MATCH p = shortestPath((origen)-[:CONECTADA*]->(destino))
        WITH origen, SUM(length(p)) AS sumatoria
        RETURN origen, CASE WHEN sumatoria > 0 THEN 1.0/sumatoria ELSE 0 END AS closeness
        ORDER BY closeness DESC
    """)
    List<ClosenessResult> closenessOf(@Param("ids") List<Long> ids);
}
