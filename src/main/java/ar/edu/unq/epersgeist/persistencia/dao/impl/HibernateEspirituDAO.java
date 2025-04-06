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

}
