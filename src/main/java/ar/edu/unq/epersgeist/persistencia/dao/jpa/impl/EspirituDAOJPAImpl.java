package ar.edu.unq.epersgeist.persistencia.dao.jpa.impl;

import ar.edu.unq.epersgeist.persistencia.dao.jpa.EspirituDAOJPA;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.espiritu.DemonioJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.espiritu.EspirituJPADTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EspirituDAOJPAImpl implements EspirituDAOJPA {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public EspirituJPADTO crear(EspirituJPADTO espiritu) {
        if (espiritu.getId() == null){
            this.entityManager.persist(espiritu);
            return espiritu;
        } else {
            return this.entityManager.merge(espiritu);
        }
    }

    @Override
    public Optional<EspirituJPADTO> recuperar(Long espirituId) {
        return Optional.ofNullable(this.entityManager.find(EspirituJPADTO.class, espirituId));
    }

    @Override
    public List<EspirituJPADTO> recuperarTodos() {
        return this.entityManager.createQuery("from espiritu", EspirituJPADTO.class).getResultList();
    }

    @Override
    public void actualizar(String nombre, int nivelDeConexion, Long id) {
        this.entityManager.createQuery("update espiritu e set e.nombre=:nombre, e.nivelDeConexion=:nivelDeConexion, e.updatedAt = current_timestamp where e.id = :id")
                .setParameter("nombre", nombre)
                .setParameter("nivelDeConexion", nivelDeConexion)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void eliminar(Long espirituId) {
        this.entityManager.createQuery("update espiritu as e set e.deleted = current_timestamp where e.id = :espirituId")
                .setParameter("espirituId", espirituId).executeUpdate();
    }

    @Override
    public List<EspirituJPADTO> todosLosEspiritusDeMedium(Long mediumId) {
        return this.entityManager.createQuery("from espiritu e where e.medium.id = :mediumId", EspirituJPADTO.class)
                .setParameter("mediumId", mediumId).getResultList();
    }

    @Override
    public List<DemonioJPADTO> espiritusDemoniacos(Sort.Direction direccion, int pagina, int cantidadPorPagina) {
        TypedQuery<DemonioJPADTO> query = this.entityManager.createQuery("from espiritu e where type(e) = demonio order by e.nivelDeConexion " + direccion.name(), DemonioJPADTO.class);
        query.setFirstResult(cantidadPorPagina * (pagina - 1));
        query.setMaxResults(cantidadPorPagina);
        return query.getResultList();
    }

    @Override
    public List<EspirituJPADTO> espiritusEn(Long ubicacionId) {
        return this.entityManager.createQuery("from espiritu e where e.ubicacion.id = :ubicacionId order by e.nombre asc", EspirituJPADTO.class)
                .setParameter("ubicacionId", ubicacionId).getResultList();
    }

    @Override
    public int cantidadTotalDeDemoniosEn(Long santuarioId) {
        return this.entityManager.createQuery("select count(e) from espiritu e where e.ubicacion.id = :santuarioId and type(e) = demonio", Long.class)
                .setParameter("santuarioId", santuarioId).getSingleResult().intValue();
    }

    @Override
    public int cantidadDeDemoniosLibresEn(Long santuarioId) {
        return this.entityManager.createQuery("select count(e) from espiritu e where e.ubicacion.id = :santuarioId and type(e) = demonio and e.medium is null", Long.class)
                .setParameter("santuarioId", santuarioId).getSingleResult().intValue();
    }

    @Override
    public boolean espirituConNombre(String nombre, Long id) {
        return this.entityManager.createQuery("select count(e) > 0 from espiritu e where e.nombre = :nombre and (e.id != :id or :id is null)", Boolean.class)
                .setParameter("nombre", nombre)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public boolean referenciaAMedium(Long mediumId) {
        return this.entityManager.createQuery("select count(e) > 0 from espiritu e where e.medium.id = :mediumId", Boolean.class)
                .setParameter("mediumId", mediumId).getSingleResult();
    }

    @Override
    public boolean referenciaAUbicacion(Long ubicacionId) {
        return this.entityManager.createQuery("select count(e) > 0 from espiritu e where e.ubicacion.id = :ubicacionId", Boolean.class)
                .setParameter("ubicacionId", ubicacionId).getSingleResult();
    }

    @Override
    public void desvincular(Long mediumId) {
        this.entityManager.createQuery("update espiritu e set e.nivelDeConexion = 0, e.medium = null where e.id = :mediumId")
                .setParameter("mediumId", mediumId).executeUpdate();
    }
}
