package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.persistencia.dao.jpa.EspirituDAOJPA;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.espiritu.EspirituJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EspirituRepository;
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
        Espiritu espirituConId = this.espirituDAOJPA.crear(espirituJpa).aModelo(espiritu);
        return espirituConId;
    }

    @Override
    public Optional<Espiritu> recuperar(Long id) {
        Optional<EspirituJPADTO> optionalEspirituJPADTO = this.espirituDAOJPA.recuperar(id);

        return optionalEspirituJPADTO.map(EspirituJPADTO::aModelo);
    }

    @Override
    public void eliminar(Long id) {
        espirituDAOJPA.eliminar(id);
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return this.espirituDAOJPA.recuperarTodos().stream().map(EspirituJPADTO::aModelo).toList();
    }

    @Override
    public boolean espirituConNombre(String nombre, Long id) {
        return this.espirituDAOJPA.espirituConNombre(nombre, id);
    }

    @Override
    public List<Demonio> espiritusDemoniacos(Sort.Direction direccion, int pagina, int cantidadPorPagina) {
       return this.espirituDAOJPA.espiritusDemoniacos(direccion, pagina, cantidadPorPagina).stream()
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
    public int cantidadTotalDeDemonios(Long idSantuario) {
        return this.espirituDAOJPA.cantidadTotalDeDemoniosEn(idSantuario);
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
    public boolean referenciaAMedium(Long id) {
        return this.espirituDAOJPA.referenciaAMedium(id);
    }

    @Override
    public boolean referenciaAUbicacion(Long id) {
        return this.espirituDAOJPA.referenciaAUbicacion(id);
    }
}
