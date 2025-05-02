package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.exception.RecursoNoEncontradoException;
import ar.edu.unq.epersgeist.persistencia.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import ar.edu.unq.epersgeist.persistencia.MediumDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class MediumServiceImpl implements MediumService {

    private final MediumDAO mediumDao;
    private final EspirituDAO espirituDao;
    private final UbicacionDAO ubicacionDao;


    public MediumServiceImpl (MediumDAO mediumDao, EspirituDAO espirituDao,UbicacionDAO ubicacionDao) {
        this.mediumDao = mediumDao;
        this.espirituDao = espirituDao;
        this.ubicacionDao = ubicacionDao;
    }

    @Override
    public Medium crear(Medium medium) {

        Ubicacion ubicacion =  ubicacionDao.findById(medium.getUbicacion().getId())
                .orElseThrow(()-> new RecursoNoEncontradoException("Ubicacion", medium.getUbicacion().getId()));

        medium.setUbicacion(ubicacion);

        return mediumDao.save(medium);

    }

    @Override
    public Optional<Medium> recuperar(Long mediumId) throws RecursoNoEncontradoException {
        Optional<Medium> optionalMedium = mediumDao.findById(mediumId);

        return mediumDao.findById(mediumId);
    }

    @Override
    public List<Medium> recuperarTodos() {

        return mediumDao.findAll();
    }

    @Override
    public  Medium actualizar(Medium updateMedium) {

        Long mediumId = updateMedium.getId();

        Medium mediumOriginal = mediumDao.findById(mediumId).orElseThrow(()-> new RecursoNoEncontradoException("Medium", mediumId));

        updateMedium.setId(mediumOriginal.getId());
        updateMedium.setAngeles(mediumOriginal.getAngeles());
        updateMedium.setDemonios(mediumOriginal.getDemonios());
        updateMedium.setUbicacion(mediumOriginal.getUbicacion());

        return mediumDao.save(updateMedium);
    }

    @Override
    public void eliminar(Long idDelMedium) {

        mediumDao.deleteById(idDelMedium);
    }

    @Override
    public void descansar(Long mediumId) {


        Optional<Medium> optionalMedium = mediumDao.findById(mediumId);

        if (optionalMedium.isEmpty()) throw new RecursoNoEncontradoException("Medium", mediumId);

        Medium medium = optionalMedium.get();
        medium.descansar();

        mediumDao.save(medium);

    }

    @Override
    public void exorcisar(Long idMediumExorcista, Long idMediumAExorcisar) {

        Optional<Medium> optionalExorcista = mediumDao.findById(idMediumExorcista);
        Optional<Medium> optionalPoseido = mediumDao.findById(idMediumAExorcisar);

        if (optionalExorcista.isEmpty()) throw new RecursoNoEncontradoException("Medium", idMediumExorcista);
        if (optionalPoseido.isEmpty()) throw new RecursoNoEncontradoException("Medium", idMediumAExorcisar);

        Medium exorcista = optionalExorcista.get();
        Medium exorcizado = optionalPoseido.get();

        exorcista.exorcizarA(exorcizado);

        mediumDao.save(exorcista);
        mediumDao.save(exorcizado);

    }

    @Override
    public List<Espiritu> espiritus(Long mediumId) {

        Optional<Medium> medium = mediumDao.findById(mediumId);
        List<Espiritu> espiritus = new ArrayList<>();

        if(medium.isEmpty()) throw new RecursoNoEncontradoException("Medium", mediumId);
        Medium currentMedium = medium.get();

        espiritus = espirituDao.todosLosEspiritusDeMedium(currentMedium.getId());

        return espiritus;

    }


    @Override
    public Espiritu invocar(Long mediumId, Long espirituId) {


        Optional<Medium> optionalMedium =  mediumDao.findById(mediumId);
        Optional<Espiritu> optionalEspiritu = espirituDao.findById(espirituId);

        if (optionalMedium.isEmpty()) throw new RecursoNoEncontradoException("Medium", espirituId);
        if (optionalEspiritu.isEmpty()) throw new RecursoNoEncontradoException("Espiritu", espirituId);

        Espiritu espiritu = optionalEspiritu.get();
        Medium medium = optionalMedium.get();

        medium.invocar(espiritu);

        mediumDao.save(medium);
        espirituDao.save(espiritu);
        return espiritu;
    }

}
