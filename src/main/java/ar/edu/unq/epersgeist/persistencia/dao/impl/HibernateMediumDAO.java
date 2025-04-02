package ar.edu.unq.epersgeist.persistencia.dao.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class HibernateMediumDAO extends HibernateDAO<Medium> implements MediumDAO {

    public HibernateMediumDAO() {
        super(Medium.class);
    }

    @Override
    public List<Medium> recuperarTodos() {
        Session session = HibernateTransactionRunner.getCurrentSession();
        String hql = "select m from Medium m order by id asc";
        Query<Medium> query = session.createQuery(hql, Medium.class);
        return query.getResultList();
    }

}