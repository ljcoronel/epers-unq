package ar.edu.unq.epersgeist.persistencia.dao.jpa;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.espiritu.EspirituJPADTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspirituDAOJPA extends JpaRepository<EspirituJPADTO, Long> {

    @Query(
            "From espiritu e where e.medium.id= :MediumId "
    )
    List<EspirituJPADTO> todosLosEspiritusDeMedium(@Param("MediumId") Long mediumId);

    @Query(
            "from espiritu e where e.ubicacion.id = :unaUbicacion order by e.nombre asc"
    )
    List<EspirituJPADTO> espiritusEn(@Param("unaUbicacion") Long ubicacionId);

    @Query(
            "select count(e) from espiritu e where e.ubicacion.id = :ubicacionID and TYPE(e) = demonio and e.medium is null"
    )
    int cantidadDeDemoniosLibresEn(@Param("ubicacionID") Long ubicacionId);

    @Query(
            "select count(e) > 0 from espiritu e where e.nombre = :otroNombre and (e.id != :otroId or :otroId is null)"
    )
    boolean espirituConNombre(@Param("otroNombre") String nombre, @Param("otroId") Long id);

    @Modifying
    @Query(
            "update espiritu e set e.nombre=:nombre, e.nivelDeConexion=:nivelDeConexion, e.updatedAt = current_timestamp where e.id = :id"
    )
    void actualizar(@Param("nombre")String nombre, @Param("nivelDeConexion")int nivelDeConexion, @Param("id")Long id);

    @Query("select count(e) from espiritu e where TYPE(e) = demonio and e.ubicacion.id = :unaUbicacion")
    int cantidadTotalDeDemonios(@Param("unaUbicacion") Long id);

    @Modifying
    @Query(value = "UPDATE espiritu SET nivel_de_conexion = 0, medium_id = null WHERE id = :unId", nativeQuery = true)
    void desvincular(Long unId);

    @Query("select count(e) > 0 from espiritu e where e.medium.id = :unaId")
    boolean referencia(@Param("unaId") Long id);
}