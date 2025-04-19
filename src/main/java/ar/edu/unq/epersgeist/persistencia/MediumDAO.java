package ar.edu.unq.epersgeist.persistencia;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;

import org.hibernate.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediumDAO extends JpaRepository<Medium, Long> {
    /*
    Medium crear(Medium medium);
    Optional<Medium> recuperar(Long mediumId);
    List<Medium> recuperarTodos();
    void actualizar(Medium medium);
    void eliminar(Long idDelMedium);
*/
}
