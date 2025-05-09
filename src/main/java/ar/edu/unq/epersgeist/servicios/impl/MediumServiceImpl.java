package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.exception.EntidadTodaviaReferenciadaException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.exception.EntidadInexistenteException;
import ar.edu.unq.epersgeist.persistencia.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import ar.edu.unq.epersgeist.persistencia.MediumDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
                .orElseThrow(()-> new EntidadInexistenteException("La Ubicacion no existe"));

        medium.setUbicacion(ubicacion);

        return mediumDao.save(medium);

    }

    @Override
    public Optional<Medium> recuperar(Long mediumId) throws EntidadInexistenteException {
        Optional<Medium> optionalMedium = mediumDao.findById(mediumId);

        return mediumDao.findById(mediumId);
    }

    @Override
    public List<Medium> recuperarTodos() {

        return mediumDao.findAll();
    }

    @Override
    public void actualizar(Medium medium) {

        mediumDao.actualizar(medium.getNombre(), medium.getManaMax(),medium.getMana(), medium.getId());
    }

    @Override
    public void eliminar(Long idDelMedium) {
        if (this.mediumDao.esReferenciado(idDelMedium)) throw new EntidadTodaviaReferenciadaException("El médium de id: " + idDelMedium + " todavía es referenciado.");
        mediumDao.deleteById(idDelMedium);
    }

    @Override
    public void descansar(Long mediumId) {

        Optional<Medium> optionalMedium = mediumDao.findById(mediumId);

        if (optionalMedium.isEmpty()) return;

        Medium medium = optionalMedium.get();
        medium.descansar();

        mediumDao.save(medium);

    }

    @Override
    public void exorcisar(Long idMediumExorcista, Long idMediumAExorcisar) {

        Optional<Medium> optionalExorcista = mediumDao.findById(idMediumExorcista);
        Optional<Medium> optionalPoseido = mediumDao.findById(idMediumAExorcisar);

        if (optionalExorcista.isEmpty()) return;

        Medium exorcista = optionalExorcista.get();

        exorcista.exorcizarA(optionalPoseido);

        Medium exorcizado = optionalPoseido.get();

        mediumDao.save(exorcista);
        mediumDao.save(exorcizado);

    }

    @Override
    public List<Espiritu> espiritus(Long mediumId) {

        Optional<Medium> medium = mediumDao.findById(mediumId);
        List<Espiritu> espiritus = new ArrayList<>();

        if(medium.isEmpty()) return null;
        Medium currentMedium = medium.get();

        espiritus = espirituDao.todosLosEspiritusDeMedium(currentMedium.getId());

        return espiritus;

    }


    @Override
    public Espiritu invocar(Long mediumId, Long espirituId) {


        Optional<Medium> optionalMedium =  mediumDao.findById(mediumId);
        Optional<Espiritu> optionalEspiritu = espirituDao.findById(espirituId);

        if (optionalMedium.isEmpty()) return optionalEspiritu.orElse(null);

        Medium medium = optionalMedium.get();

        medium.invocar(optionalEspiritu);

        Espiritu espiritu = optionalEspiritu.get();

        mediumDao.save(medium);
        espirituDao.save(espiritu);
        return espiritu;
    }

}
