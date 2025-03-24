package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.jdbc.JDBCEspirituDAO;
import ar.edu.unq.epersgeist.servicios.EspirituService;

import java.util.List;

public class EspirituServiceImpl implements EspirituService {

    private JDBCEspirituDAO espirituDao = new JDBCEspirituDAO();

    @Override
    public Espiritu crear(Espiritu espiritu) {
        return getEspirituDao().crear(espiritu);
    }

    @Override
    public Espiritu recuperar(Long espirituId) {
        return getEspirituDao().recuperar(espirituId);
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return getEspirituDao().recuperarTodos();
    }

    @Override
    public void actualizar(Espiritu espiritu) {
        getEspirituDao().actualizar(espiritu);
    }

    @Override
    public void eliminar(Long espirituId) {
        getEspirituDao().eliminar(espirituId);
    }

    @Override
    public Medium conectar(Long espirituId, Medium medium) {
        Espiritu espiritu = recuperar(espirituId);
        medium.conectarseAEspiritu(espiritu);
        espiritu.aumentarConexion(medium);

        getEspirituDao().actualizar(espiritu); //garantiza ACID
        return medium;
    }

    public JDBCEspirituDAO getEspirituDao() {
        return espirituDao;
    }

    public void setEspirituDao(JDBCEspirituDAO espirituDao) {
        this.espirituDao = espirituDao;
    }
}
