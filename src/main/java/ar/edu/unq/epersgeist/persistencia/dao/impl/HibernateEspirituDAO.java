package ar.edu.unq.epersgeist.persistencia.dao.impl;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;


public class HibernateEspirituDAO extends HibernateDAO<Espiritu> implements EspirituDAO {

    public HibernateEspirituDAO() {
        super(Espiritu.class);
    }

}
