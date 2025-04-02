package ar.edu.unq.epersgeist.persistencia.dao.impl;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class HibernateEspirituDAO extends HibernateDAO<Espiritu> implements EspirituDAO {

    public HibernateEspirituDAO() {
        super(Espiritu.class);
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return List.of();
    }

//    @Override
//    public List<Espiritu> espiritusDeTipo(String tipo) {
//        Session session = HibernateTransactionRunner.getCurrentSession();
//        String hql = "from Espiritu * where dtype == :tipo";
//        Query<Espiritu> query = session.createQuery(hql, Espiritu.class);
//        query.setParameter("tipo", tipo);
//        return query.getResultList();
//    }

}
