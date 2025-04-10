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

    public List<Medium> mediumsSinEspiritusEn(Long ubicacionId) {
        Session session = HibernateTransactionRunner.getCurrentSession();
        String hql = "from Medium m where m.ubicacion.id = :unLugarDado and m.espiritus is empty order by m.nombre asc";
        Query<Medium> query = session.createQuery(hql, Medium.class);
        query.setParameter("unLugarDado", ubicacionId);
        return query.getResultList();
    }
}