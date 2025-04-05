package ar.edu.unq.epersgeist.persistencia.dao.impl;

import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class HibernateDAO<T> {

    private final Class<T> entityType;

    public HibernateDAO(Class<T> entityType) {
        this.entityType = entityType;
    }

    public void crear(T entity) {
        Session session = HibernateTransactionRunner.getCurrentSession();
        session.persist(entity);
    }

    public T recuperar(Long id) {
        Session session = HibernateTransactionRunner.getCurrentSession();
        return Optional.ofNullable(session.get(entityType, id)).orElseThrow(PersistenceException::new);
    }

    public void actualizar(T entity) {
        Session session = HibernateTransactionRunner.getCurrentSession();
        session.merge(entity);
    }
    public void eliminar(T entity) {
        Session session = HibernateTransactionRunner.getCurrentSession();
        session.remove(entity);
    }

    public List<T> recuperarTodos() {
        Session session = HibernateTransactionRunner.getCurrentSession();
        return session.createQuery("from " + entityType.getSimpleName(), entityType).list();
    }
}