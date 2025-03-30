package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;

import java.util.List;

public interface EspirituDAO {
    void guardar(Espiritu espiritu);
    Espiritu recuperar(Long idDelEspiritu);
    List<Espiritu> recuperarTodos();
    void eliminar(Espiritu espiritu);
//    List<Espiritu> espiritusDemoniacos(); o List<Espiritu> espiritusDeTipo(TipoEspiritu tipo); ??
}