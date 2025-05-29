package ar.edu.unq.epersgeist.persistencia.dao.jpa.impl;

import ar.edu.unq.epersgeist.persistencia.dao.jpa.MediumDAOJPA;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.MediumJPADTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MediumDAOJPAImpl implements MediumDAOJPA {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public MediumJPADTO crear(MediumJPADTO medium) {
        if (medium.getId() == null){
            this.entityManager.persist(medium);
            return medium;
        } else {
            return this.entityManager.merge(medium);
        }
    }

    @Override
    public Optional<MediumJPADTO> recuperar(Long mediumId) {
        return Optional.ofNullable(this.entityManager.find(MediumJPADTO.class, mediumId));
    }

    @Override
    public List<MediumJPADTO> recuperarTodos() {
        return this.entityManager.createQuery("from medium", MediumJPADTO.class).getResultList();
    }

    @Override
    public void actualizar(String nombre, int manaMax, int mana, Long id) {
        this.entityManager.createQuery("update medium m set m.nombre=:nombre, m.manaMax=:manaMax, m.mana=:mana, m.updatedAt = current_timestamp where m.id = :id")
                .setParameter("nombre", nombre)
                .setParameter("manaMax", manaMax)
                .setParameter("mana", mana)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void eliminar(Long mediumId) {
        this.entityManager.createQuery("update medium as m set m.deleted = current_timestamp where m.id = :mediumId")
                .setParameter("mediumId", mediumId).executeUpdate();
    }

    @Override
    public List<MediumJPADTO> mediumsSinEspiritusEn(Long ubicacionId) {
        return this.entityManager.createQuery("from medium m where m.ubicacion.id = :ubicacionId and m.angeles is empty and m.demonios is empty order by m.nombre asc", MediumJPADTO.class)
                .setParameter("ubicacionId", ubicacionId).getResultList();
    }

    @Override
    public MediumJPADTO mediumConMasDemoniosEn(Long santuarioId) {
        try {
            return this.entityManager.createQuery("select m from medium m, espiritu e where m.id = e.medium.id and m.ubicacion.id = :santuarioId group by m order by sum(case when type(e) = demonio then 1 else 0 end) desc limit 1", MediumJPADTO.class)
                    .setParameter("santuarioId", santuarioId).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public boolean referenciaAUbicacion(Long ubicacionId) {
        return this.entityManager.createQuery("select count(m) > 0 from medium m where m.ubicacion.id = :ubicacionId", Boolean.class)
                .setParameter("ubicacionId", ubicacionId).getSingleResult();
    }
}
