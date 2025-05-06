package ar.edu.unq.epersgeist.persistencia;

import ar.edu.unq.epersgeist.modelo.Medium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediumDAO extends JpaRepository<Medium, Long> {
    @Query("from Medium m where m.ubicacion.id = :unaUbicacion and m.angeles is empty and m.demonios is empty order by m.nombre asc")
    List<Medium> mediumsSinEspiritusEn(@Param("unaUbicacion") Long ubicacionId);

    @Modifying
    @Query(
            "update Medium m set m.nombre=:nombre, m.manaMax=:manaMax,m.mana=:mana where m.id = :id"
    )
    void actualizar(@Param("nombre")String nombre, @Param("manaMax")int manaMax,@Param("mana")int mana, @Param("id")Long id);

    @Query(
            "select e.medium from Espiritu e where e.ubicacion.id = :unaUbicacion and TYPE(e) = Demonio and e.medium is not null group by e.medium order by count(e) desc"
    )
    Medium mediumConMasDemoniosEn(@Param("unaUbicacion") Long ubicacionId);
}
