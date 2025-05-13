package ar.edu.unq.epersgeist.persistencia.dao.jpa;

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
            "update Medium m set m.nombre=:nombre, m.manaMax=:manaMax,m.mana=:mana, m.updatedAt = current_timestamp where m.id = :id"
    )
    void actualizar(@Param("nombre")String nombre, @Param("manaMax")int manaMax,@Param("mana")int mana, @Param("id")Long id);

    @Query(value = "SELECT m.id FROM medium m JOIN espiritu e ON m.id = e.medium_id WHERE m.ubicacion_id = :ubicacionId AND e.deleted is null GROUP BY m.id ORDER BY SUM(CASE WHEN e.dtype = 'Demonio' THEN 1 ELSE 0 END) desc limit 1", nativeQuery = true)
    Long mediumConMasDemoniosEn(@Param("ubicacionId") Long ubicacionId);

    @Query("select count(e) > 0 from Medium m join Espiritu e on e.medium.id = :unaId")
    boolean esReferenciado(@Param("unaId") Long id);
}
