package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.exception.EntidadTodaviaReferenciadaException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.exception.EntidadInexistenteException;
import ar.edu.unq.epersgeist.persistencia.DAO.jpa.EspirituDAOJPA;
import ar.edu.unq.epersgeist.persistencia.DAO.jpa.UbicacionDAOJPA;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import ar.edu.unq.epersgeist.persistencia.DAO.jpa.MediumDAOJPA;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MediumServiceImpl implements MediumService {

    private final MediumDAOJPA mediumDAOJPA;
    private final EspirituDAOJPA espirituDAOJPA;
    private final UbicacionDAOJPA ubicacionDAOJPA;


    public MediumServiceImpl (MediumDAOJPA mediumDAOJPA, EspirituDAOJPA espirituDAOJPA, UbicacionDAOJPA ubicacionDAOJPA) {
        this.mediumDAOJPA = mediumDAOJPA;
        this.espirituDAOJPA = espirituDAOJPA;
        this.ubicacionDAOJPA = ubicacionDAOJPA;
    }

    @Override
    public Medium crear(Medium medium) {

        Ubicacion ubicacion =  ubicacionDAOJPA.findById(medium.getUbicacion().getId())
                .orElseThrow(()-> new EntidadInexistenteException("La Ubicacion no existe"));

        medium.setUbicacion(ubicacion);

        return mediumDAOJPA.save(medium);

    }

    @Override
    public Optional<Medium> recuperar(Long mediumId) throws EntidadInexistenteException {
        Optional<Medium> optionalMedium = mediumDAOJPA.findById(mediumId);

        return mediumDAOJPA.findById(mediumId);
    }

    @Override
    public List<Medium> recuperarTodos() {

        return mediumDAOJPA.findAll();
    }

    @Override
    public void actualizar(Medium medium) {

        mediumDAOJPA.actualizar(medium.getNombre(), medium.getManaMax(),medium.getMana(), medium.getId());
    }

    @Override
    public void eliminar(Long idDelMedium) {
        if (this.mediumDAOJPA.esReferenciado(idDelMedium)) throw new EntidadTodaviaReferenciadaException("El médium de id: " + idDelMedium + " todavía es referenciado.");
        mediumDAOJPA.deleteById(idDelMedium);
    }

    @Override
    public void descansar(Long mediumId) {

        Optional<Medium> optionalMedium = mediumDAOJPA.findById(mediumId);

        if (optionalMedium.isEmpty()) return;

        Medium medium = optionalMedium.get();
        medium.descansar();

        mediumDAOJPA.save(medium);

    }

    @Override
    public void exorcisar(Long idMediumExorcista, Long idMediumAExorcisar) {

        Optional<Medium> optionalExorcista = mediumDAOJPA.findById(idMediumExorcista);
        Optional<Medium> optionalPoseido = mediumDAOJPA.findById(idMediumAExorcisar);

        if (optionalExorcista.isEmpty()) return;

        Medium exorcista = optionalExorcista.get();

        exorcista.exorcizarA(optionalPoseido);

        Medium exorcizado = optionalPoseido.get();

        mediumDAOJPA.save(exorcista);
        mediumDAOJPA.save(exorcizado);

    }

    @Override
    public List<Espiritu> espiritus(Long mediumId) {

        Optional<Medium> medium = mediumDAOJPA.findById(mediumId);
        List<Espiritu> espiritus;

        if(medium.isEmpty()) return null;
        Medium currentMedium = medium.get();

        espiritus = espirituDAOJPA.todosLosEspiritusDeMedium(currentMedium.getId());

        return espiritus;

    }


    @Override
    public Espiritu invocar(Long mediumId, Long espirituId) {


        Optional<Medium> optionalMedium =  mediumDAOJPA.findById(mediumId);
        Optional<Espiritu> optionalEspiritu = espirituDAOJPA.findById(espirituId);

        if (optionalMedium.isEmpty()) return optionalEspiritu.orElse(null);

        Medium medium = optionalMedium.get();

        medium.invocar(optionalEspiritu);

        Espiritu espiritu = optionalEspiritu.get();

        mediumDAOJPA.save(medium);
        espirituDAOJPA.save(espiritu);
        return espiritu;
    }

    @Override
    public void mover(Long mediumId, Long ubicacionId) {
        Optional<Medium> optionalMedium = mediumDAOJPA.findById(mediumId);

        if (optionalMedium.isEmpty()) return;

        Medium medium = optionalMedium.get();

        Optional<Ubicacion> ubicacion = ubicacionDAOJPA.findById(ubicacionId);

        medium.mover(ubicacion);

        mediumDAOJPA.save(medium);

    }

}
