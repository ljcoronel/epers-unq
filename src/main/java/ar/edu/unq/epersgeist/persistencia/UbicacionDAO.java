package ar.edu.unq.epersgeist.persistencia;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UbicacionDAO extends JpaRepository<Ubicacion, Long> {

    @Query("select count(u) > 0 from Ubicacion u where u.nombre = :unaUbicacion")
    boolean existeNombre(@Param("unaUbicacion") String nombre);
}
