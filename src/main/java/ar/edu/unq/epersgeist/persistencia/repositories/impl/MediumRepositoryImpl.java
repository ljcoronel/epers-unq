package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.jpa.MediumDAOJPA;
import ar.edu.unq.epersgeist.persistencia.dao.neo4j.MediumDAONEO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.MediumNEODTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.MediumJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.MediumRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MediumRepositoryImpl implements MediumRepository {
    private final MediumDAOJPA mediumDAOJPA;
    private final MediumDAONEO mediumDAONEO;

    public MediumRepositoryImpl(MediumDAOJPA mediumDAOJPA, MediumDAONEO mediumDAONEO) {
        this.mediumDAOJPA = mediumDAOJPA;
        this.mediumDAONEO = mediumDAONEO;
    }

    @Override
    public void actualizar(Medium medium) {
        mediumDAOJPA.actualizar(medium.getNombre(), medium.getManaMax(),medium.getMana(), medium.getId());
        mediumDAONEO.save(MediumNEODTO.desdeModelo(medium));
    }

    @Override
    public Medium guardar(Medium medium) {
        MediumJPADTO mediumJPA = MediumJPADTO.desdeModelo(medium);
        Medium mediumConId = this.mediumDAOJPA.save(mediumJPA).aModelo(medium);

        MediumNEODTO mediumNEO = MediumNEODTO.desdeModelo(mediumConId);
        mediumDAONEO.save(mediumNEO);

        return mediumConId;
    }

    @Override
    public Optional<Medium> recuperar(Long id) {
        Optional<MediumJPADTO> optionalMediumJPADTO = this.mediumDAOJPA.findById(id);
        return optionalMediumJPADTO.map(MediumJPADTO::aModelo);
    }

    @Override
    public void eliminar(Long id) {
        this.mediumDAOJPA.deleteById(id);
        this.mediumDAONEO.deleteById(id);
    }

    @Override
    public List<Medium> recuperarTodos() {
        return this.mediumDAOJPA.findAll().stream().map(MediumJPADTO::aModelo).toList();
    }

    @Override
    public List<Medium> mediumsSinEspiritusEn(Long ubicacionId) {
        return this.mediumDAOJPA.mediumsSinEspiritusEn(ubicacionId).stream().map(MediumJPADTO::aModelo).toList();
    }

    @Override
    public Long mediumConMasDemonios(Long idSantuario) {
        return this.mediumDAONEO.mediumConMasDemonios(idSantuario);
    }
}
