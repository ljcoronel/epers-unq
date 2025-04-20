package ar.edu.unq.epersgeist.persistencia;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspirituDAO extends JpaRepository<Espiritu, Long> {

    @Query(
            "FROM Espiritu e WHERE e.medium.id = :idDelMedium"
    )
    List<Espiritu> todosLosEspiritusDeMedium(@Param("idDelMedium") Long mediumId);

}
