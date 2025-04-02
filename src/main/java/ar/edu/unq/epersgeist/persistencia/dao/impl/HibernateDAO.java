package ar.edu.unq.epersgeist.persistencia.dao.impl;


import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;
import org.hibernate.Session;

import java.util.List;

public class HibernateDAO<T> {

    private final Class<T> entityType;

    public HibernateDAO(Class<T> entityType) {
        this.entityType = entityType;
    }

    public void guardar(T entity) {
        Session session = HibernateTransactionRunner.getCurrentSession();
        session.persist(entity);
    }

    public T recuperar(Long id) {
        Session session = HibernateTransactionRunner.getCurrentSession();
        return session.get(entityType, id);
    }

    public void eliminar(T entity) {
        Session session = HibernateTransactionRunner.getCurrentSession();
        session.remove(entity);
    }

    public List<T> recuperarTodos() {
        Session session = HibernateTransactionRunner.getCurrentSession();
        return session.createQuery("from " + entityType.getSimpleName(), entityType).list();

    }

    public void actualizar() {
        Session session = HibernateTransactionRunner.getCurrentSession();
        session.persist(entityType);

    }
}