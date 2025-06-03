package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.persistencia.dao.jpa.EspirituDAOJPA;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.espiritu.EspirituJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EspirituRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EspirituRepositoryImpl implements EspirituRepository {

    private final EspirituDAOJPA espirituDAOJPA;

    public EspirituRepositoryImpl(EspirituDAOJPA espirituDAOJPA) {
        this.espirituDAOJPA = espirituDAOJPA;
    }

    @Override
    public void actualizar(Espiritu espiritu) {
        this.espirituDAOJPA.actualizar(espiritu.getNombre(), espiritu.getNivelDeConexion(), espiritu.getId());
    }

    @Override
    public Espiritu guardar(Espiritu espiritu) {
        EspirituJPADTO espirituJpa = EspirituJPADTO.desdeModelo(espiritu);
        Espiritu espirituConId = this.espirituDAOJPA.save(espirituJpa).aModelo(espiritu);
        return espirituConId;
    }

    @Override
    public Optional<Espiritu> recuperar(Long id) {
        Optional<EspirituJPADTO> optionalEspirituJPADTO = this.espirituDAOJPA.findById(id);
        return optionalEspirituJPADTO.map(EspirituJPADTO::aModelo);
    }

    @Override
    public void eliminar(Long id) {
        espirituDAOJPA.deleteById(id);
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return this.espirituDAOJPA.findAll().stream().map(EspirituJPADTO::aModelo).toList();
    }

    @Override
    public boolean espirituConNombre(String nombre, Long id) {
        return this.espirituDAOJPA.espirituConNombre(nombre, id);
    }

    @Override
    public List<Demonio> espiritusDemoniacos(Sort.Direction direccion, int pagina, int cantidadPorPagina) {
       return this.espirituDAOJPA.findAll
                       (Example.of
                               (EspirituJPADTO.desdeModelo(new Demonio())),
                               PageRequest.of(pagina,cantidadPorPagina, direccion, "nivelDeConexion"))
                                .map(dto -> (Demonio) dto.aModelo()).toList();
    }

    @Override
    public List<Espiritu> todosLosEspiritusDeMedium(Long id) {
        return this.espirituDAOJPA.todosLosEspiritusDeMedium(id).stream().map(EspirituJPADTO::aModelo).toList();
    }

    @Override
    public List<Espiritu> espiritusEn(Long ubicacionId) {
        return this.espirituDAOJPA.espiritusEn(ubicacionId).stream().map(EspirituJPADTO::aModelo).toList();
    }

    @Override
    public Long santuarioCorrupto() {
        return this.espirituDAOJPA.santuarioCorrupto();
    }

    @Override
    public int cantidadTotalDeDemonios(Long idSantuario) {
        return this.espirituDAOJPA.cantidadTotalDeDemonios(idSantuario);
    }

    @Override
    public int cantidadDeDemoniosLibresEn(Long idSantuario) {
        return this.espirituDAOJPA.cantidadDeDemoniosLibresEn(idSantuario);
    }

    @Override
    public void desvincular(Long id) {
        this.espirituDAOJPA.desvincular(id);
    }

    @Override
    public boolean referencia(Long id) {
        return this.espirituDAOJPA.referencia(id);
    }
}
