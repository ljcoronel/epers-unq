package ar.edu.unq.epersgeist.persistencia.dao.impl;

import ar.edu.unq.epersgeist.modelo.Direccion;
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
    public <T extends Espiritu> List<T> espiritusDeTipo(Direccion direccion, int pagina, int cantidadPorPagina, Class<T> tipo) {
        if (pagina < 0) throw new IllegalArgumentException("Página debe ser positivo.");
        if (cantidadPorPagina <= 0) throw new IllegalArgumentException("La cantidad por página debe ser positiva y mayor a 0.");

        int offset = cantidadPorPagina * pagina;

        Session session = HibernateTransactionRunner.getCurrentSession();
        String hql = "from " + tipo.getSimpleName() + " order by nivelDeConexion " + direccion.value + " limit " + cantidadPorPagina + " offset " + offset;
        return session.createQuery(hql, tipo).list();
    }

}
