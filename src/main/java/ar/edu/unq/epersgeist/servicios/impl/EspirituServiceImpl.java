package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.PortalEspiritus;
import ar.edu.unq.epersgeist.modelo.exception.espiritu.EspirituMuyLejanoException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.exception.EntidadInexistenteException;
import ar.edu.unq.epersgeist.modelo.exception.NombreRepetidoException;
import ar.edu.unq.epersgeist.modelo.exception.ParametroIncorrectoException;
import ar.edu.unq.epersgeist.persistencia.repositories.impl.EspirituRepositoryImpl;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.MediumRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.exception.TemperaturaMuyAltaException;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class EspirituServiceImpl implements EspirituService {

    private final EspirituRepository espirituRepository;
    private final MediumRepository mediumRepository;
    private final UbicacionRepository ubicacionRepository;
//    private final ArrayList<PortalEspiritus> portalesAbiertos;

    public EspirituServiceImpl(EspirituRepositoryImpl espirituRepository, MediumRepository mediumRepository, UbicacionRepository ubicacionRepository) {
        this.espirituRepository = espirituRepository;
        this.mediumRepository = mediumRepository;
        this.ubicacionRepository = ubicacionRepository;
//        this.portalesAbiertos = new ArrayList<>();
    }


    @Override
    public Espiritu crear(Espiritu espiritu) {

        verificarNombreRepetido(espiritu);

        Ubicacion ubicacion = ubicacionRepository.encontrarUbicacionQueContiene(espiritu.getCoordenada())
                .orElseThrow(()-> new EntidadInexistenteException("No se encontró la ubicación del espíritu."));

        espiritu.setUbicacion(ubicacion);

        return this.espirituRepository.guardar(espiritu);

    }

    private void verificarNombreRepetido(Espiritu espiritu) {
        if (this.existeConNombre(espiritu)) throw new NombreRepetidoException("Ya existe un espíritu con el nombre: " + espiritu.getNombre());
    }

    @Override
    public Optional<Espiritu> recuperar(Long espirituId) {
        return this.espirituRepository.recuperar(espirituId);
    }

    @Override
    public List<Espiritu> recuperarTodos() {

        return this.espirituRepository.recuperarTodos();
    }

    @Override
    public void actualizar(Espiritu espiritu) {

        verificarNombreRepetido(espiritu);

        this.espirituRepository.actualizar(espiritu);
    }

    @Override
    public void eliminar(Long espirituId) {
        this.espirituRepository.eliminar(espirituId);
    }

    @Override
    public List<Demonio> espiritusDemoniacos(Sort.Direction direccion, int pagina, int cantidadPorPagina) {
        if (pagina < 0) throw new ParametroIncorrectoException("El valor página debe ser positivo o 0.");
        if (cantidadPorPagina <= 0) throw new ParametroIncorrectoException("La cantidad por página debe ser positiva."); // porque no se debería hacer una consulta a la base de datos para traerse nada
        return this.espirituRepository.espiritusDemoniacos(direccion, pagina, cantidadPorPagina);
    }

    @Override
    public Medium conectar(Long espirituId, Long mediumId) {

            Optional<Medium> optionalMedium = mediumRepository.recuperar(mediumId);

            Optional<Espiritu> optionalEspiritu = this.espirituRepository.recuperar(espirituId);

            if (optionalEspiritu.isEmpty()) return optionalMedium.orElse(null);
            Espiritu espiritu = optionalEspiritu.get();

            espiritu.conectarConMedium(optionalMedium);

            Medium medium = optionalMedium.get();

            mediumRepository.guardar(medium);


            return medium;

    }


    @Override
    public boolean existeConNombre(Espiritu espiritu) {
        return this.espirituRepository.espirituConNombre(espiritu.getNombre(), espiritu.getId());
    }

    @Override
    public void dominar(Long espirituDominanteId, Long espirituADominarId) {

        if(Objects.equals(espirituDominanteId, espirituADominarId)) return;

        Optional<Espiritu> optEspDominante = espirituRepository.recuperar(espirituDominanteId);
        Optional<Espiritu> optEspADominar = espirituRepository.recuperar(espirituADominarId);

        if(optEspDominante.isEmpty() || optEspADominar.isEmpty() ) return;

        Espiritu espirituDominante = optEspDominante.get();
        Espiritu espirituADominar = optEspADominar.get();

        if (!espirituRepository.estaEntreDistancia(espirituDominante.getId(), espirituADominar.getCoordenada(), 2, 5)) throw new EspirituMuyLejanoException("Los espíritus deben estar entre 2 y 5 km de distancia.");

        espirituADominar.serDominadoPor(espirituDominante);

        espirituRepository.guardar(espirituADominar);

    }

    @Override
    public PortalEspiritus crearPortal(PortalEspiritus portal) {

        Point punto = portal.getCoordenada();

        ubicacionRepository.encontrarUbicacionQueContiene(punto).orElseThrow(
                () -> new EntidadInexistenteException("No se encontró ubicación en el punto dado."));

        verificarTemperaturaBaja(punto);

        Demonio demonioHost =
                this.espirituRepository
                        .demonioLibreNoDominadoMasFuerteEn(punto.getX(), punto.getY())
                        .orElseThrow(() -> new EntidadInexistenteException("No se encontró un host válido."));  // TODO tal vez tomar un radio en vez de una ubicación entera?

        portal.setDemonioHost(demonioHost);


        new Thread(() -> activarPortal(demonioHost, portal)).start(); // TODO revisar cuando se reinstancia un portal (no hay limit en JPQL)

        return portal;
    }

    private void iniciarPortal(Espiritu demonioHost, PortalEspiritus portal) {
        portal.abrirPortal();
        espirituRepository.guardar(demonioHost); // su nivel de conex pasa a 0
    }

    private void activarPortal(Espiritu demonioHost, PortalEspiritus portal) {
//        portalesAbiertos.add(portal);
        iniciarPortal(demonioHost, portal);
        while(portal.sigueAbierto()) { // TODO falta instanciar al último minion con el lvl de conex que haya
            try {
                espirituRepository.guardar(demonioHost); // actualiza al host
                this.crear(portal.obtenerSiguienteMinion()); } catch (InterruptedException e) {throw new RuntimeException(e);}
                // crea al minion
        }
//        portalesAbiertos.remove(portal);
    }



    private void verificarTemperaturaBaja(Point punto) {
        if(!ubicacionRepository.esTemperaturaMenorOIgualA(punto.getX(), punto.getY(), -5) ) throw new TemperaturaMuyAltaException("La temperatura debe ser más baja para abrir el portal.");
    }

}
