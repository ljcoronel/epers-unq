package ar.edu.unq.epersgeist.persistencia.dao.jpa;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspirituDAO extends JpaRepository<Espiritu, Long> {

    @Query(
            "From Espiritu e where e.medium.id= :MediumId "
    )
    List<Espiritu> todosLosEspiritusDeMedium(@Param("MediumId") Long mediumId);

    @Query(
            "from Espiritu e where e.ubicacion.id = :unaUbicacion order by e.nombre asc"
    )
    List<Espiritu> espiritusEn(@Param("unaUbicacion") Long ubicacionId);

    @Query(
            "select count(e) from Espiritu e where e.ubicacion.id = :ubicacionID and TYPE(e) = Demonio and e.medium is null"
    )
    int cantidadDeDemoniosLibresEn(@Param("ubicacionID") Long ubicacionId);

    @Query(
            "select count(e) > 0 from Espiritu e where e.nombre = :otroNombre and (e.id != :otroId or :otroId is null)"
    )
    boolean espirituConNombre(@Param("otroNombre") String nombre, @Param("otroId") Long id);

    @Modifying
    @Query(
            "update Espiritu e set e.nombre=:nombre, e.nivelDeConexion=:nivelDeConexion, e.updatedAt = current_timestamp where e.id = :id"
    )
    void actualizar(@Param("nombre")String nombre, @Param("nivelDeConexion")int nivelDeConexion, @Param("id")Long id);

    @Query(value = "SELECT u.id FROM espiritu e JOIN ubicacion u ON e.ubicacion_id = u.id WHERE u.dtype = 'Santuario' AND e.deleted is null GROUP BY u.id ORDER BY SUM(CASE WHEN e.dtype = 'Demonio' THEN 1 ELSE 0 END) - SUM(CASE WHEN e.dtype = 'Angel' THEN 1 ELSE 0 END) DESC LIMIT 1", nativeQuery = true)
    Long santuarioCorrupto();

    @Query("select count(e) from Espiritu e where type(e) = Demonio and e.ubicacion.id = :unaUbicacion")
    int cantidadTotalDeDemonios(@Param("unaUbicacion") Long id);

}