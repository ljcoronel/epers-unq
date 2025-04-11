package ar.edu.unq.epersgeist.persistencia.dao.impl;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;
import org.hibernate.Session;
import org.hibernate.query.Order;
import org.hibernate.query.Query;
import org.hibernate.query.SortDirection;

import java.util.List;

public class HibernateEspirituDAO extends HibernateDAO<Espiritu> implements EspirituDAO {

    public HibernateEspirituDAO() {
        super(Espiritu.class);
    }

    @Override
    public <T extends Espiritu> List<T> espiritusDeTipo(SortDirection direccion, int pagina, int cantidadPorPagina, Class<T> clase) {
        int offset = cantidadPorPagina * pagina;

        Session session = HibernateTransactionRunner.getCurrentSession();
        String hql = "from " + clase.getSimpleName();

        Query<T> query = session.createQuery(hql, clase);
        query.setOrder(Order.by(clase, "nivelDeConexion", direccion));
        query.setFirstResult(offset);
        query.setMaxResults(cantidadPorPagina);


        return query.list();
    }

    public List<Espiritu> espiritusEn(Long ubicacionId) {
        Session session = HibernateTransactionRunner.getCurrentSession();
        String hql = "from Espiritu e where e.ubicacion.id = :unLugarDado order by e.nombre asc";
        Query<Espiritu> query = session.createQuery(hql, Espiritu.class);
        query.setParameter("unLugarDado", ubicacionId);
        return query.getResultList();
    }
}
