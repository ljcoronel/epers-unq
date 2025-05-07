package ar.edu.unq.epersgeist.persistencia;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UbicacionDAO extends JpaRepository<Ubicacion, Long> {

    @Query("select count(u) > 0 from Ubicacion u where u.nombre = :unaUbicacion and (u.id != :unId or :unId is null)")
    boolean existeNombre(@Param("unaUbicacion") String nombre, @Param("unId") Long unId);

    @Modifying
    @Query("update Ubicacion set nombre = :unNombre where id = :unaId")
    void actualizar(@Param("unaId") Long id, @Param("unNombre") String nombre);
}
