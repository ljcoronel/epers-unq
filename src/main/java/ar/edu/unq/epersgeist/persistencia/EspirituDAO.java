package ar.edu.unq.epersgeist.persistencia;

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
            "select count(e) > 0 from Espiritu e where e.nombre = :otroNombre and (e.id != :otroId or :otroId is null)"
    )
    boolean espirituConNombre(@Param("otroNombre") String nombre, @Param("otroId") Long id);

    @Modifying
    @Query(
            "update Espiritu e set e.nombre=:nombre, e.nivelDeConexion=:nivelDeConexion where e.id = :id"
    )
    void actualizar(@Param("nombre")String nombre, @Param("nivelDeConexion")int nivelDeConexion, @Param("id")Long id);
}