package ar.edu.unq.epersgeist.persistencia.dao.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import java.util.List;

public class HibernateMediumDAO extends HibernateDAO<Medium> implements MediumDAO {

    public HibernateMediumDAO() {
        super(Medium.class);
    }

    @Override
    public List<Medium> recuperarTodos() {
        return List.of();
    }

}