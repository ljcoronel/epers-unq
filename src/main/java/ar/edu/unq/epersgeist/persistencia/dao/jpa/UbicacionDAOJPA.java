package ar.edu.unq.epersgeist.persistencia.dao.jpa;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.UbicacionJPADTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UbicacionDAOJPA extends JpaRepository<UbicacionJPADTO, Long> {

    @Query("select count(u) > 0 from ubicacion u where u.nombre = :unaUbicacion and (u.id != :unId or :unId is null)")
    boolean existeNombre(@Param("unaUbicacion") String nombre, @Param("unId") Long unId); // TODO va el dto?

    @Modifying
    @Query("update ubicacion u set u.nombre = :unNombre, u.energia = :cantEnergia, u.updatedAt = current_timestamp where u.id = :unaId")
    void actualizar(@Param("unaId") Long id, @Param("unNombre") String nombre, @Param("cantEnergia") int energia); // TODO va el dto?

    @Query("select (exists (select 1 from Medium m where m.ubicacion.id = u.id) " +
            "or exists (select 1 from Espiritu e where e.ubicacion.id = u.id)) from ubicacion u where u.id = :unaId") // TODO va el dto?
    boolean esReferenciado(@Param("unaId") Long id);

}
