package ar.edu.unq.epersgeist.persistencia;

import ar.edu.unq.epersgeist.modelo.Medium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediumDAO extends JpaRepository<Medium, Long> {
    @Query("from Medium m where m.ubicacion.id = :unaUbicacion and m.angeles is empty and m.demonios is empty order by m.nombre asc")
    List<Medium> mediumsSinEspiritusEn(@Param("unaUbicacion") Long ubicacionId);
}
