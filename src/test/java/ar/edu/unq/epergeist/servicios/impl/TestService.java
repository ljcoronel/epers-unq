package ar.edu.unq.epergeist.servicios.impl;

import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;
import org.hibernate.Session;

public class TestService {

    public void eliminarModelo(Class clase) {
        HibernateTransactionRunner.runTrx(() -> {
            Session session = HibernateTransactionRunner.getCurrentSession();
            session.createQuery("delete from " + clase.getSimpleName()).executeUpdate();
            return null;
        });
    }
}