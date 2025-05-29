package ar.edu.unq.epersgeist.persistencia.dao.jpa.impl;

import ar.edu.unq.epersgeist.persistencia.dao.jpa.UbicacionDAOJPA;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.ubicacion.SantuarioJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.ubicacion.UbicacionJPADTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UbicacionDAOJPAImpl implements UbicacionDAOJPA {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UbicacionJPADTO crear(UbicacionJPADTO ubicacion) {
        if (ubicacion.getId() == null){
            this.entityManager.persist(ubicacion);
            return ubicacion;
        } else {
            return this.entityManager.merge(ubicacion);
        }
    }

    @Override
    public Optional<UbicacionJPADTO> recuperar(Long ubicacionId) {
        return Optional.ofNullable(this.entityManager.find(UbicacionJPADTO.class, ubicacionId));
    }

    @Override
    public List<UbicacionJPADTO> recuperarTodos() {
        return this.entityManager.createQuery("from ubicacion", UbicacionJPADTO.class).getResultList();
    }

    @Override
    public void actualizar(Long id, String nombre, int energia) {
        this.entityManager.createQuery("update ubicacion u set u.nombre=:nombre, u.energia=:energia, u.updatedAt = current_timestamp where u.id = :id")
                .setParameter("nombre", nombre)
                .setParameter("energia", energia)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void eliminar(Long ubicacionId) {
        this.entityManager.createQuery("update ubicacion as u set u.deleted = current_timestamp where u.id = :ubicacionId")
                .setParameter("ubicacionId", ubicacionId).executeUpdate();
    }

    @Override
    public SantuarioJPADTO santuarioCorrupto() {
        try {
            return this.entityManager.createQuery("select u from ubicacion u, espiritu e where u.id = e.ubicacion.id and type(u) = santuario group by u order by sum(case when type(e) = demonio then 1 else 0 end) - sum(case when type(e) = angel then 1 else 0 end) desc limit 1", SantuarioJPADTO.class).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public boolean ubicacionConNombre(String nombre, Long id) {
        return this.entityManager.createQuery("select count(u) > 0 from ubicacion u where u.nombre = :nombre and (u.id != :id or :id is null)", Boolean.class)
                .setParameter("nombre", nombre)
                .setParameter("id", id)
                .getSingleResult();
    }
}
