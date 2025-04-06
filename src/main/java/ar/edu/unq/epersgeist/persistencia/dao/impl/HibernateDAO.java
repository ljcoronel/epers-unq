package ar.edu.unq.epersgeist.persistencia.dao.impl;


import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class HibernateDAO<T> {

    private final Class<T> entityType;

    public HibernateDAO(Class<T> entityType) {
        this.entityType = entityType;
    }

    public T crear(T entity) {
        Session session = HibernateTransactionRunner.getCurrentSession();
        session.persist(entity);
        return entity;
    }

    public Optional<T> recuperar(Long id) {
        Session session = HibernateTransactionRunner.getCurrentSession();
        T objetoRecuperado = session.get(entityType, id);
        return Optional.ofNullable(objetoRecuperado);
    }

    public void eliminar(Long id) {
        Session session = HibernateTransactionRunner.getCurrentSession();
        T objetoRecuperado = session.get(entityType, id); // consigo el objeto para poder eliminarlo (ya que entra por id)
        if (objetoRecuperado == null) return; // si no existe, no hago nada
        session.remove(objetoRecuperado); // si existe, lo elimino
    }

    public List<T> recuperarTodos() {
        Session session = HibernateTransactionRunner.getCurrentSession();
        return session.createQuery("from " + entityType.getSimpleName(), entityType).list();

    }

    public void actualizar(T entity) {
        Session session = HibernateTransactionRunner.getCurrentSession();
        session.merge(entity);
    }
}