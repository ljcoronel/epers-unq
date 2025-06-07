package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.persistencia.dao.jpa.EspirituDAOJPA;
import ar.edu.unq.epersgeist.persistencia.dao.mongodb.EspirituDAOMONGO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.EspirituMONGODTO;
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
    private final EspirituDAOMONGO espirituDAOMongo;

    public EspirituRepositoryImpl(EspirituDAOJPA espirituDAOJPA, EspirituDAOMONGO espirituDAOMongo) {
        this.espirituDAOMongo = espirituDAOMongo;
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

        EspirituMONGODTO espirituMongo= EspirituMONGODTO.desdeModelo(espirituConId);
        this.espirituDAOMongo.save(espirituMongo);

        return espirituConId;
    }

    @Override
    public Optional<Espiritu> recuperar(Long id) {
        Optional<EspirituJPADTO> optionalEspirituJPADTO = this.espirituDAOJPA.findById(id);

        return espirituDAOJPA.findById(id).map(jpaDTO->{
            Espiritu espiritu = jpaDTO.aModelo();
            EspirituMONGODTO espirituMongo = this.espirituDAOMongo.findById(id).get();
            return espirituMongo.aModelo(espiritu);
        });
    }

    @Override
    public void eliminar(Long id) {
        espirituDAOJPA.deleteById(id);
        this.espirituDAOMongo.deleteById(id);
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return this.espirituDAOJPA.findAll().stream().map(jpaDTO-> {
            Espiritu espiritu = jpaDTO.aModelo();
            EspirituMONGODTO espirituMongo = this.espirituDAOMongo.findById(espiritu.getId()).get();
            return espirituMongo.aModelo(espiritu);
        }).toList();
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
                                .map(jpaDTO-> {
                                    Demonio espiritu = (Demonio) jpaDTO.aModelo();
                                    EspirituMONGODTO espirituMongo = this.espirituDAOMongo.findById(espiritu.getId()).get();
                                    return (Demonio) espirituMongo.aModelo(espiritu);
                                }).toList();
    }

    @Override
    public List<Espiritu> todosLosEspiritusDeMedium(Long id) {
        return this.espirituDAOJPA.todosLosEspiritusDeMedium(id).stream().map(this::mappearEspiritu).toList();
    }

    @Override
    public List<Espiritu> espiritusEn(Long ubicacionId) {
        return this.espirituDAOJPA.espiritusEn(ubicacionId).stream().map(this::mappearEspiritu).toList();
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

    private Espiritu mappearEspiritu(EspirituJPADTO espirituJPADTO){

        Espiritu espiritu = espirituJPADTO.aModelo();
        EspirituMONGODTO espirituMongo = this.espirituDAOMongo.findById(espiritu.getId()).get();
        return espirituMongo.aModelo(espiritu);
    }
}
