package ar.edu.unq.epersgeist.persistencia.dao.neo4j;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.ClosenessResultDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.DegreeResultDTO;
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

    @Query("""
        UNWIND $ids AS id
        MATCH (u:UBICACION {id: id})
        WITH COLLECT(u) AS ubicaciones
        UNWIND ubicaciones AS origen
        UNWIND ubicaciones AS destino
        WITH origen, destino
        WHERE origen.id <> destino.id
        MATCH p = shortestPath((origen)-[:CONECTADA*]->(destino))
        WITH origen, SUM(length(p)) AS sumatoria
        WITH origen, CASE WHEN sumatoria > 0 THEN 1.0/sumatoria ELSE 0 END AS closeness
        ORDER BY closeness DESC
        RETURN origen, closeness
    """)
    List<ClosenessResultDTO> closenessOf(@Param("ids") List<Long> ids);

    @Query("""
            MATCH (n:UBICACION)
            WITH count(n) AS total_nodos
            MATCH (u:UBICACION) where u.id in $ids
            OPTIONAL MATCH (u)-[:CONECTADA]->(a:UBICACION)
            WITH
              u AS origen,
              collect(a.id) AS conectados,
              size(collect(a.id)) AS cantidad_conectados,
              count(a) AS cantidad_conexiones,
              total_nodos
            ORDER BY cantidad_conectados desc
            RETURN
              origen,
              toFloat(cantidad_conectados) / toFloat(total_nodos - 1) AS degree
            """)
    List<DegreeResultDTO> outgoingDegreeOf(@Param("ids") List<Long> ids);


    @Query("""
            MATCH (n:UBICACION)
            WITH count(n) AS total_nodos
            MATCH (u:UBICACION) where u.id in $ids
            OPTIONAL MATCH (u)<-[:CONECTADA]-(a:UBICACION)
            WITH
              u AS origen,
              collect(a.id) AS conectados,
              size(collect(a.id)) AS cantidad_conectados,
              count(a) AS cantidad_conexiones,
              total_nodos
            ORDER BY cantidad_conectados desc
            RETURN
              origen,
              toFloat(cantidad_conectados) / toFloat(total_nodos - 1) AS degree
            """)
    List<DegreeResultDTO> incomingDegreeOf(@Param("ids") List<Long> ids);



}
