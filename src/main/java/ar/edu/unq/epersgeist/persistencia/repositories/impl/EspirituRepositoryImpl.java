package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.persistencia.dao.jpa.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.neo4j.EspirituDAONEO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.espiritu.EspirituNEODTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.espiritu.EspirituJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EspirituRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Setter @Getter
@Repository
public class EspirituRepositoryImpl implements EspirituRepository {

    private final EspirituDAO espirituDAOJPA;
    private final EspirituDAONEO espirituDAONEO;

    public EspirituRepositoryImpl(EspirituDAO espirituDAOJPA, EspirituDAONEO espirituDAONEO) {
        this.espirituDAOJPA = espirituDAOJPA;
        this.espirituDAONEO = espirituDAONEO;
    }

    @Override
    public void actualizar(Espiritu espiritu) {
        getEspirituDAOJPA().actualizar(espiritu.getNombre(), espiritu.getNivelDeConexion(), espiritu.getId());

        espirituDAONEO.actualizar(
                espiritu.getId(),
                espiritu.getNombre());
    }

    @Override
    public Espiritu guardar(Espiritu espiritu) {
        EspirituJPADTO espirituJpa = EspirituJPADTO.desdeModelo(espiritu);
        Espiritu espirituConId = this.espirituDAOJPA.save(espirituJpa).aModelo(espiritu);

        EspirituNEODTO espirituNeo = EspirituNEODTO.desdeModelo(espirituConId);
        this.espirituDAONEO.save(espirituNeo);

        return espirituConId;
    }

    @Override
    public Optional<Espiritu> recuperar(Long id) {
        Optional<EspirituJPADTO> optionalEspirituJPADTO = getEspirituDAOJPA().findById(id);

        return optionalEspirituJPADTO.map(EspirituJPADTO::aModelo);
    }

    @Override
    public void eliminar(Long id) {
        espirituDAONEO.deleteById(id);
        espirituDAOJPA.deleteById(id);
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return getEspirituDAOJPA().findAll().stream().map(EspirituJPADTO::aModelo).toList();
    }

    @Override
    public boolean espirituConNombre(String nombre, Long id) {
        return getEspirituDAOJPA().espirituConNombre(nombre, id);
    }

    @Override
    public List<Demonio> espiritusDemoniacos(Sort.Direction direccion, int pagina, int cantidadPorPagina) {
       return getEspirituDAOJPA().findAll
                       (Example.of
                               (EspirituJPADTO.desdeModelo(new Demonio())),
                               PageRequest.of(pagina,cantidadPorPagina, direccion, "nivelDeConexion"))
                                .map(dto -> (Demonio) dto.aModelo()).toList();
    }

    @Override
    public List<Espiritu> todosLosEspiritusDeMedium(Long id) {
        return getEspirituDAOJPA().todosLosEspiritusDeMedium(id).stream().map(EspirituJPADTO::aModelo).toList();
    }

    @Override
    public List<Espiritu> espiritusEn(Long ubicacionId) {
        return getEspirituDAOJPA().espiritusEn(ubicacionId).stream().map(EspirituJPADTO::aModelo).toList();
    }

    @Override
    public Long santuarioCorrupto() {
        return getEspirituDAOJPA().santuarioCorrupto();
    }

    @Override
    public int cantidadTotalDeDemonios(Long idSantuario) {
        return getEspirituDAOJPA().cantidadTotalDeDemonios(idSantuario);
    }

    @Override
    public int cantidadDeDemoniosLibresEn(Long idSantuario) {
        return getEspirituDAOJPA().cantidadDeDemoniosLibresEn(idSantuario);
    }

    @Override
    public void desvincular(Long id) {
        getEspirituDAOJPA().desvincular(id);
    }
}
