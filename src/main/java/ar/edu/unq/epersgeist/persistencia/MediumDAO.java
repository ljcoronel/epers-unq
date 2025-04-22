package ar.edu.unq.epersgeist.persistencia;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import org.hibernate.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediumDAO extends JpaRepository<Medium, Long> {
    //    Medium crear(Medium medium);
//    Optional<Medium> recuperar(Long mediumId);
//    List<Medium> recuperarTodos();
//    void actualizar(Medium medium);
//    void eliminar(Long idDelMedium);
    @Query("from Medium m where m.ubicacion.id = :unaUbicacion and m.angeles is empty and m.demonios is empty order by m.nombre asc")
    List<Medium> mediumsSinEspiritusEn(@Param("unaUbicacion") Long ubicacionId);
}
