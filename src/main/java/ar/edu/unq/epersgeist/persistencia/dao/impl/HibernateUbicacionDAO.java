package ar.edu.unq.epersgeist.persistencia.dao.impl;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;

public class HibernateUbicacionDAO extends HibernateDAO<Ubicacion> implements UbicacionDAO {

    public HibernateUbicacionDAO() { super(Ubicacion.class); }
}
