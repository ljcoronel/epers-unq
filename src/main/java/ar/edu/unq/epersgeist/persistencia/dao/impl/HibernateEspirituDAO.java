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
    public <T extends Espiritu> List<T> espiritusDeTipo(Class<T> tipo) {
        Session session = HibernateTransactionRunner.getCurrentSession();
        String hql = "from " + tipo.getSimpleName() + " order by nivelDeConexion desc";
        return session.createQuery(hql, tipo).list();
    }

}
