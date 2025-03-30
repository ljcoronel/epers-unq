package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.servicios.EspirituService;

import java.util.List;

public class EspirituServiceImpl implements EspirituService {

    private EspirituDAO espirituDao;

    @Override
    public Espiritu crear(Espiritu espiritu) {
//        return getEspirituDao().crear(espiritu);
        return null;
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
//        getEspirituDao().actualizar(espiritu);
    }

    @Override
    public void eliminar(Long espirituId) {
//        getEspirituDao().eliminar(espirituId);
    }

    @Override
    public List<Espiritu> espiritusDemoniacos() {
        return List.of();
    }

    @Override
    public Medium conectar(Long espirituId, Long mediumId) {
//        Espiritu espiritu = recuperar(espirituId);
//
//        medium.conectarseAEspiritu(espiritu);
//
//        getEspirituDao().actualizar(espiritu); //garantiza ACID
//        return medium;
        return null;
    }

    public EspirituDAO getEspirituDao() {
        return espirituDao;
    }

    private void setEspirituDao(EspirituDAO espirituDao) {
        this.espirituDao = espirituDao;
    }
}
