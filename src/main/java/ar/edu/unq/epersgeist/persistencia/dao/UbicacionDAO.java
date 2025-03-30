package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Ubicacion;

import java.util.List;

public interface UbicacionDAO {
    Ubicacion crear(Ubicacion ubicacion);
    Ubicacion recuperar(Long ubicacionId);
    List<Ubicacion> recuperarTodos();
    void actualizar(Ubicacion ubicacion);
    void eliminar(Long ubicacionId);
}
