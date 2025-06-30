package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.PortalDemonios;
import ar.edu.unq.epersgeist.modelo.geo.Coordenada;
import ar.edu.unq.epersgeist.modelo.exception.EntidadInexistenteException;
import ar.edu.unq.epersgeist.modelo.exception.NombreRepetidoException;
import ar.edu.unq.epersgeist.modelo.exception.EntidadTodaviaReferenciadaException;
import ar.edu.unq.epersgeist.modelo.exception.ubicacion.UbicacionesNoConectadasException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.persistencia.ClosenessResult;
import ar.edu.unq.epersgeist.persistencia.DegreeResult;
import ar.edu.unq.epersgeist.persistencia.dao.influxdb.UbicacionDAOINFLUX;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.influx.EventoPortalDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.exception.AreaOcupadaException;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import ar.edu.unq.epersgeist.testUtils.TestService;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.neo4j.core.Neo4jClient;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.LongStream;

import static java.time.Instant.now;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class UbicacionServiceTest {

    @Autowired
    private UbicacionService ubicacionService;

    @Autowired
    private EspirituService espirituService;

    @Autowired
    private MediumService mediumService;

    @Autowired
    private UbicacionDAOINFLUX ubicacionDAOINFLUX; // TODO sacar, se debería poder hacer con el service

    @Autowired
    private UbicacionRepository ubicacionRepository; // TODO sacar

    private List<Coordenada> pointsUbicacionUno;
    private List<Coordenada> pointsUbicacionDos;
    private List<Coordenada> pointsUbicacionTres;
    private List<Coordenada> pointsUbicacionCuatro;
    private List<Coordenada> pointsUbicacionCinco;
    private List<Coordenada> pointsCementerioUno;

    private Ubicacion ubicacionUno;
    private Ubicacion ubicacionDos;
    private Ubicacion ubicacionTres;

    private Santuario ubicacionCuatro;
    private Santuario ubicacionCinco;

    private Cementerio cementerioUno;
    private Cementerio cementerioDos = new Cementerio("Chocolandia", 50,List.of(
            new Coordenada(30.1, 30.1),
                new Coordenada(40, 30.1),
                new Coordenada(40, 40),
                new Coordenada(30.1, 40),
                new Coordenada(30.1, 30.1)), 20);

    private Santuario santuarioUno = new Santuario("Tailandia", 50,List.of(
            new Coordenada(20, 20),
            new Coordenada(20, 30),
            new Coordenada(30, 30),
            new Coordenada(30, 20),
            new Coordenada(20, 20)), 50);


    private Angel angelUno;

    private Demonio demonioUno;
    private Demonio demonioDos;

    private Medium mediumUno;
    private Medium mediumDos;
    private Medium mediumTres;

    @Autowired
    private TestService limpiador;

    @Autowired
    private Neo4jClient neo4jClient;



    @BeforeEach
    void prepare() {
        pointsUbicacionUno= List.of(
                new Coordenada(0, 0),
                new Coordenada(1, 0),
                new Coordenada(1, 1),
                new Coordenada(0, 1),
                new Coordenada(0, 0)
        );

        pointsUbicacionDos= List.of(
                new Coordenada(3, 3),
                new Coordenada(4, 3),
                new Coordenada(4, 4),
                new Coordenada(3, 4),
                new Coordenada(3, 3));

        pointsUbicacionTres = List.of(
                new Coordenada(6, 0),
                new Coordenada(7, 0),
                new Coordenada(7, 1),
                new Coordenada(6, 1),
                new Coordenada(6, 0)
        );

        pointsUbicacionCuatro = List.of(
                new Coordenada(0, 5),
                new Coordenada(1, 5),
                new Coordenada(1, 6),
                new Coordenada(0, 6),
                new Coordenada(0, 5)
        );

        pointsUbicacionCinco = List.of(
                new Coordenada(5, 5),
                new Coordenada(6, 5),
                new Coordenada(6, 6),
                new Coordenada(5, 6),
                new Coordenada(5, 5)
        );

        pointsCementerioUno = List.of(
                new Coordenada(20, 20),
                new Coordenada(20, 30),
                new Coordenada(30, 30),
                new Coordenada(30, 20),
                new Coordenada(20, 20));


        this.ubicacionUno = new Santuario("Bernal", 50);
        this.ubicacionDos = new Santuario("Quilmes", 50);
        this.ubicacionTres = new Santuario("Avellaneda", 50);
        this.ubicacionCuatro = new Santuario("Chocolandia", 75);
        this.ubicacionCinco = new Santuario("MiCasa", 30);

        this.cementerioUno = new Cementerio("Noselandia", 30);

        ubicacionUno.setTerritorio(pointsUbicacionUno);
        ubicacionUno.setTerritorio(pointsUbicacionUno);
        ubicacionDos.setTerritorio(pointsUbicacionDos);
        ubicacionTres.setTerritorio(pointsUbicacionTres);
        ubicacionCuatro.setTerritorio(pointsUbicacionCuatro);
        ubicacionCinco.setTerritorio(pointsUbicacionCinco);

        cementerioUno.setTerritorio(pointsCementerioUno);

        ubicacionUno.establecerTemperatura(5);
        ubicacionDos.establecerTemperatura(10);
        ubicacionTres.establecerTemperatura(20);
        ubicacionCuatro.establecerTemperatura(25);
        ubicacionCinco.establecerTemperatura(50);

        this.angelUno = new Angel(10, "angelical",new Coordenada(0.5, 0.5));

        this.demonioUno = new Demonio(10, "el yose",new Coordenada(0.5, 0.5));
        this.demonioDos = new Demonio(10, "repepe",new Coordenada(0.5, 0.5));

        this.mediumUno = new Medium("Fabian", 100, 10, new Coordenada(0.5, 0.5));
        this.mediumDos = new Medium("Franco", 200, 50, new Coordenada(0.5, 0.5));
        this.mediumTres = new Medium("Tobias", 300, 60, new Coordenada(0.5, 0.5));


    }

    private Instant fechaFija(int diasAtras, int minutosDespues, int segundosDespues) {
        LocalDateTime base = LocalDateTime.of(2025, 6, 21, 12, 0);
        return base
                .minusDays(diasAtras)
                .plusMinutes(minutosDespues)
                .plusSeconds(segundosDespues)
                .atZone(ZoneId.systemDefault())
                .toInstant();
    }

    @Test
    public void sePersisteUnaUbicacionEnLaBaseDeDatos() {
        Ubicacion ubicacionPersistida = this.ubicacionService.crear(ubicacionUno);
        assertEquals(this.ubicacionUno.getId(), ubicacionPersistida.getId());
        assertEquals(this.ubicacionUno.getNombre(), ubicacionPersistida.getNombre());
    }

    @Test
    public void sePersisteDosUbicacionesConElMismoNombreEntoncesNoSePuedePersistir() {
        ubicacionDos.setNombre("Bernal");
        this.ubicacionService.crear(this.ubicacionUno);

        assertThrows(NombreRepetidoException.class, () -> this.ubicacionService.crear(this.ubicacionDos));
    }

    @Test
    public void seIntentaRecuperarUnaUbicacionNoPersistidaYNoDevuelveNada() {
        this.ubicacionUno.setId(1L);

        assertFalse(this.ubicacionService.recuperar(this.ubicacionUno.getId()).isPresent());
    }

    @Test
    public void seActualizaUnaUbicacionPersistidaEnLaBaseDeDatos() {
        Ubicacion ubicacionPersistida = this.ubicacionService.crear(this.ubicacionUno);
        this.ubicacionUno.setNombre("Quilmes");
        this.ubicacionService.actualizar(this.ubicacionUno);

        assertEquals(this.ubicacionUno.getNombre(), ubicacionPersistida.getNombre());
        assertEquals(this.ubicacionUno.getId(), ubicacionPersistida.getId());
    }

    @Test
    public void seIntentaActualizarElNombredeUnaUbicacionYaRegistradaEnLaBaseDeDatosEntoncesLanzaUnaExcepcion() {
        this.ubicacionService.crear(this.ubicacionUno);
        this.ubicacionService.crear(this.ubicacionTres);
        this.ubicacionTres.setNombre("Bernal");

        assertThrows(NombreRepetidoException.class, () -> this.ubicacionService.actualizar(this.ubicacionTres));
    }

    @Test
    public void seIntentaActualizarUnaUbicacionQueNoExisteEnLaBaseDeDatosEntoncesNoSeActualiza() {
        this.ubicacionUno.setId(1L);
        this.ubicacionUno.setNombre("Quilmes");
        this.ubicacionService.actualizar(this.ubicacionUno);

        assertFalse(this.ubicacionService.recuperar(this.ubicacionUno.getId()).isPresent());
    }

    @Test
    public void seEliminaUnaUbicacionPersistidaEnLaBaseDeDatosPorLoQueYaNoExiste() {
        this.ubicacionService.crear(this.ubicacionUno);
        this.ubicacionService.eliminar(this.ubicacionUno.getId());

        assertFalse(this.ubicacionService.recuperar(this.ubicacionUno.getId()).isPresent());
    }

    @Test
    public void noSePuedeEliminarUnaUbicacionTodaviaReferenciada() {
        this.ubicacionService.crear(this.ubicacionUno);
        this.mediumService.crear(mediumUno);

        assertThrows(EntidadTodaviaReferenciadaException.class, () -> this.ubicacionService.eliminar(this.ubicacionUno.getId()));
    }

    @Test
    public void seRecuperanTodasLasUbicacionesPersistidasDeLaBasesDeDatos() {
        this.ubicacionDos.setNombre("Quilmes");

        this.ubicacionService.crear(this.ubicacionUno);
        this.ubicacionService.crear(this.ubicacionDos);
        this.ubicacionService.crear(this.ubicacionTres);

        List<Ubicacion> ubicaciones = this.ubicacionService.recuperarTodos();

        assertEquals(3, ubicaciones.size());
    }

    @Test
    public void seRecuperanLasUbicacionesDeUnaBaseDeDatosVaciaYNoDevuelveNingunaUbicacion() {
        List<Ubicacion> ubicaciones = this.ubicacionService.recuperarTodos();

        assertEquals(0, ubicaciones.size());
    }


    @Test
    public void seObtieneLosMediumsDeUnaUbicacionQueNoTenganEspiritusConectados() {
        this.ubicacionService.crear(this.ubicacionUno);
        this.mediumService.crear(this.mediumUno);
        this.mediumService.crear(this.mediumDos);
        this.mediumService.crear(this.mediumTres);

        espirituService.crear(this.angelUno);
        espirituService.crear(this.demonioUno);

        espirituService.conectar(this.angelUno.getId(), mediumUno.getId());
        espirituService.conectar(this.demonioUno.getId(), mediumDos.getId());

        this.mediumService.actualizar(this.mediumUno);
        this.mediumService.actualizar(this.mediumDos);
        this.mediumService.actualizar(this.mediumTres);

        List<Medium> mediumsSinEspiritus = this.ubicacionService.mediumsSinEspiritusEn(this.ubicacionUno.getId());

        assertEquals(1, mediumsSinEspiritus.size());
        assertEquals(this.mediumTres.getId(),mediumsSinEspiritus.getFirst().getId());
    }

    @Test
    public void seVerificaQueUnAngelNoPuedeCambiarDeUbicacionSiEstaConectadoAUnMedium() {
        Ubicacion ubicacion1 = new Santuario("ElClerigo",50, pointsUbicacionUno, 15);
        Ubicacion ubicacion2 = new Santuario("Masedonia",50, pointsUbicacionDos, 15);

        ubicacionService.crear(ubicacion1);
        ubicacionService.crear(ubicacion2);
        Medium medium = new Medium("Franco", 200, 50, new Coordenada(0.5, 0.5));
        Espiritu espiritu = new Angel(10, "angelical",new Coordenada(0.5, 0.5));
        espirituService.crear(espiritu);
        mediumService.crear(medium);

        espirituService.conectar(espiritu.getId(), medium.getId());
        espiritu.setUbicacion(ubicacion2);

        assertNotEquals(espiritu.getUbicacion().getNombre(), ubicacion1.getNombre());
    }

    @Test
    public void seIntentaObtenerLosEspiritusDeUnaUbicacionQueNoExisteEnLaBaseDeDatosYDevuelveUnaListaVacia() {
        this.ubicacionUno.setId(1L);

        assertTrue(this.ubicacionService.espiritusEn(this.ubicacionUno.getId()).isEmpty());
    }

    @Test
    public void seIntentaObtenerLosMediumsSinEspiritusDeUnaUbicacionQueNoExisteEnLaBaseDeDatosYDevuelveUnaListaVacia() {
        this.ubicacionUno.setId(1L);

        assertTrue(this.ubicacionService.mediumsSinEspiritusEn(this.ubicacionUno.getId()).isEmpty());
    }

    @Test
    public void unaUbicacionNoPuedeSerActualizadaConMasDe100DeEnergia() {
        Ubicacion ubicacion = this.ubicacionService.crear(this.ubicacionUno);
        ubicacion.setEnergia(900);
        ubicacionService.actualizar(ubicacion);
        Ubicacion ubicacionActualizada = ubicacionService.recuperar(ubicacion.getId()).get();

        assertEquals(100, ubicacionActualizada.getEnergia());
    }

    @Test
    public void unaUbicacionNoPuedeSerActualizadaConMenosDe0DeEnergia() {
        Ubicacion ubicacion = this.ubicacionService.crear(this.ubicacionUno);
        ubicacion.setEnergia(-50);
        ubicacionService.actualizar(ubicacion);
        Ubicacion ubicacionActualizada = ubicacionService.recuperar(ubicacion.getId()).get();

        assertEquals(0, ubicacionActualizada.getEnergia());
    }

    @Test
    public void unaUbicacionPersisteSuEnergia() {

        Ubicacion ubicacion = this.ubicacionService.crear(ubicacionCuatro);
        Ubicacion ubicacionRecuperada = ubicacionService.recuperar(ubicacion.getId()).get();

        assertEquals(75, ubicacionRecuperada.getEnergia());
    }

    @Test
    public void unaUbicacionPersisteSuConexionAOtra() {
        Ubicacion ubicacion1 = this.ubicacionService.crear(ubicacionUno);
        Ubicacion ubicacion3 = this.ubicacionService.crear(ubicacionTres);

        ubicacionService.conectar(ubicacion1.getId(), ubicacion3.getId());

        assertTrue(ubicacionService.estanConectadas(ubicacion1.getId(), ubicacion3.getId()));

    }

    @Test
    public void unaUbicacionPersisteVariasConexiones() {
        Ubicacion ubicacion1 = this.ubicacionService.crear(ubicacionUno);
        Ubicacion ubicacion3 = this.ubicacionService.crear(ubicacionTres);
        Ubicacion ubicacion4 = this.ubicacionService.crear(ubicacionCuatro);

        ubicacionService.conectar(ubicacion1.getId(), ubicacion3.getId());
        ubicacionService.conectar(ubicacion1.getId(), ubicacion4.getId());

        assertTrue(ubicacionService.estanConectadas(ubicacion1.getId(), ubicacion3.getId()));

        assertTrue(ubicacionService.estanConectadas(ubicacion1.getId(), ubicacion4.getId()));
    }

    @Test
    public void unaUbicacionNoPuedeConectarseDosVecesALaMismaUbicacion() {
        Ubicacion ubicacion1 = this.ubicacionService.crear(ubicacionUno);
        Ubicacion ubicacion3 = this.ubicacionService.crear(ubicacionTres);

        ubicacionService.conectar(ubicacion1.getId(), ubicacion3.getId());
        ubicacionService.conectar(ubicacion1.getId(), ubicacion3.getId());

        String query = """
                RETURN COUNT {(ub1:UBICACION) -[r:CONECTADA]-> (ub2:UBICACION)}
                """;
        Map<String, Object> result = neo4jClient.query(query).fetch().first().get();
        assertEquals(1L, result.values().stream().findFirst().get());
    }

    @Test
    public void unaUbicacionPersisteConexionesBidireccionales() {
        Ubicacion ubicacion1 = this.ubicacionService.crear(ubicacionUno);
        Ubicacion ubicacion3 = this.ubicacionService.crear(ubicacionTres);

        ubicacionService.conectar(ubicacion1.getId(), ubicacion3.getId());
        ubicacionService.conectar(ubicacion3.getId(), ubicacion1.getId());

        Ubicacion ubicacion1Recuperada = ubicacionService.recuperar(ubicacion1.getId()).get();
        Ubicacion ubicacion3Recuperada = ubicacionService.recuperar(ubicacion3.getId()).get();


    }

    @Test
    public void unaUbicacionPersisteConexionesBidireccionalesEntreVariasUbicaciones() {
        Ubicacion ubicacion1 = this.ubicacionService.crear(ubicacionUno);
        Ubicacion ubicacion3 = this.ubicacionService.crear(ubicacionTres);
        Ubicacion ubicacion4 = this.ubicacionService.crear(ubicacionCuatro);

        ubicacionService.conectar(ubicacion1.getId(), ubicacion3.getId());
        ubicacionService.conectar(ubicacion1.getId(), ubicacion4.getId());

        ubicacionService.conectar(ubicacion3.getId(), ubicacion1.getId());
        ubicacionService.conectar(ubicacion3.getId(), ubicacion4.getId());

        ubicacionService.conectar(ubicacion4.getId(), ubicacion1.getId());
        ubicacionService.conectar(ubicacion4.getId(), ubicacion3.getId());

        assertTrue(ubicacionService.estanConectadas(ubicacion1.getId(), ubicacion3.getId()));
        assertTrue(ubicacionService.estanConectadas(ubicacion1.getId(), ubicacion4.getId()));

        assertTrue(ubicacionService.estanConectadas(ubicacion3.getId(), ubicacion1.getId()));
        assertTrue(ubicacionService.estanConectadas(ubicacion3.getId(), ubicacion4.getId()));

        assertTrue(ubicacionService.estanConectadas(ubicacion4.getId(), ubicacion1.getId()));
        assertTrue(ubicacionService.estanConectadas(ubicacion4.getId(), ubicacion3.getId()));

    }


    @Test
    public void unaUbicacionNoPuedeConectarseAOtraQueNoExiste() {
        Ubicacion ubicacion1 = this.ubicacionService.crear(ubicacionUno);

        assertThrows(EntidadInexistenteException.class, () -> ubicacionService.conectar(ubicacion1.getId(), -80L));
    }

    @Test
    public void unaUbicacionEstaConectadaConOtra() {
        Ubicacion ubicacion1 = this.ubicacionService.crear(ubicacionUno);
        Ubicacion ubicacion3 = this.ubicacionService.crear(ubicacionTres);

        ubicacionService.conectar(ubicacion1.getId(), ubicacion3.getId());

        assertTrue(ubicacionService.estanConectadas(ubicacion1.getId(), ubicacion3.getId()));
    }

    @Test
    public void unaUbicacionConectadaBidireccionalmenteEstanConectadasEntreSi() {
        Ubicacion ubicacion1 = this.ubicacionService.crear(ubicacionUno);
        Ubicacion ubicacion3 = this.ubicacionService.crear(ubicacionTres);

        ubicacionService.conectar(ubicacion1.getId(), ubicacion3.getId());
        ubicacionService.conectar(ubicacion3.getId(), ubicacion1.getId());

        assertTrue(ubicacionService.estanConectadas(ubicacion1.getId(), ubicacion3.getId()));
        assertTrue(ubicacionService.estanConectadas(ubicacion3.getId(), ubicacion1.getId()));
    }

    @Test
    public void unaUbicacionNoEstaConectadaConOtra() {
        Ubicacion ubicacion1 = this.ubicacionService.crear(ubicacionUno);
        Ubicacion ubicacion3 = this.ubicacionService.crear(ubicacionTres);

        assertFalse(ubicacionService.estanConectadas(ubicacion1.getId(), ubicacion3.getId()));
    }

    @Test
    public void unaUbicacionOrigenNoEstaConectadaConOtraInexistenteDestino() {
        Ubicacion ubicacion1 = this.ubicacionService.crear(ubicacionUno);

        assertFalse(ubicacionService.estanConectadas(ubicacion1.getId(), -1L));
    }

    @Test
    public void unaUbicacionDestinoNoEstaConectadaConOtraInexistenteOrigen() {
        Ubicacion ubicacion1 = this.ubicacionService.crear(ubicacionUno);

        assertFalse(ubicacionService.estanConectadas(-1L, ubicacion1.getId()));
    }

    @Test
    public void unaUbicacionEstaConectadaConsigoMisma() {
        Ubicacion ubicacion1 = this.ubicacionService.crear(ubicacionUno);

        assertTrue(ubicacionService.estanConectadas(ubicacion1.getId(), ubicacion1.getId()));
    }

    @Test
    public void unaUbicacionQueNoExisteNoEstaConectadaConLaMismaQueNoExiste() {

        assertFalse(ubicacionService.estanConectadas(-1L, -1L));
    }

    @Test
    public void alBuscarElCaminoMasCortoAUnaUbicacionRetornaUnaListaDelCaminoMasCortoDeUbicaciones(){
        Ubicacion ubicacion1 = this.ubicacionService.crear(ubicacionUno);
        Ubicacion ubicacion2 = this.ubicacionService.crear(ubicacionDos);
        Ubicacion ubicacion3 = this.ubicacionService.crear(ubicacionTres);
        Ubicacion ubicacion4 = this.ubicacionService.crear(ubicacionCuatro);
        Ubicacion ubicacion5 = this.ubicacionService.crear(ubicacionCinco);

        ubicacionService.conectar(ubicacion1.getId(), ubicacion2.getId());
        ubicacionService.conectar(ubicacion2.getId(), ubicacion3.getId());
        ubicacionService.conectar(ubicacion3.getId(), ubicacion4.getId());

        ubicacionService.conectar(ubicacion1.getId(), ubicacion5.getId());
        ubicacionService.conectar(ubicacion5.getId(), ubicacion4.getId());

        List<Ubicacion> camino = ubicacionService.caminoMasCorto(ubicacion1.getId(),ubicacion4.getId());

        assertEquals(2,camino.size());//no contamos al nodo origen
    }

    @Test
    public void alBuscarElCaminoMasCortoEntreDosUbicacionesQueNoEstanConectadasDeNingunaFormaRetornaUnaExcepcion(){
        Ubicacion ubicacion1 = this.ubicacionService.crear(ubicacionUno);
        Ubicacion ubicacion2 = this.ubicacionService.crear(ubicacionDos);

        assertThrows(UbicacionesNoConectadasException.class, () -> ubicacionService.caminoMasCorto(ubicacion1.getId(),ubicacion2.getId()));
    }
    @Test
    public void alBuscarElCaminoMasCortoSiendoDestinoYOrigenLaMismaUbicacionRetornaUnaListaVacia(){
        Ubicacion ubicacion1 = this.ubicacionService.crear(ubicacionUno);

        List<Ubicacion> camino = ubicacionService.caminoMasCorto(ubicacion1.getId(),ubicacion1.getId());

        assertEquals(0,camino.size());//no contamos al nodo origen
    }

    @Test
    public void alBuscarElCaminoMasCortoConUnaUbicacionOrigenQueNoExisteRetornaNull(){
        Ubicacion ubicacion1 = this.ubicacionService.crear(ubicacionUno);

        assertNull(ubicacionService.caminoMasCorto(-1L,ubicacion1.getId()));

    }

    @Test
    public void alBuscarElCaminoMasCortoConUnaUbicacionDestinoQueNoExisteRetornaException(){
        Ubicacion ubicacion1 = this.ubicacionService.crear(ubicacionUno);

        assertThrows(EntidadInexistenteException.class, () -> ubicacionService.caminoMasCorto(ubicacion1.getId(),-1L));

    }

    @Test
    public void alBuscarElCaminMasCortoDondeSoloDestinoEstaConectadaConOrigenRetornaUnaExeption(){
        Ubicacion ubicacion1 = this.ubicacionService.crear(ubicacionUno);
        Ubicacion ubicacion2 = this.ubicacionService.crear(ubicacionDos);

        ubicacionService.conectar(ubicacion2.getId(), ubicacion1.getId());

        assertThrows(UbicacionesNoConectadasException.class, () -> ubicacionService.caminoMasCorto(ubicacion1.getId(),ubicacion2.getId()));
    }

    @Test
    public void alRecuperarUbicacionesSobracargadasSeObtieneUnaListaDeUbicacionesQueSobrepasanElUmbralDado() {
        Ubicacion ubicacion1 = this.ubicacionService.crear(ubicacionUno);
        Ubicacion ubicacion2 = this.ubicacionService.crear(ubicacionDos);
        Ubicacion ubicacion3 = this.ubicacionService.crear(ubicacionTres);

        ubicacion1.setEnergia(20);
        ubicacion2.setEnergia(30);
        ubicacion3.setEnergia(40);

        ubicacionService.actualizar(ubicacion1);
        ubicacionService.actualizar(ubicacion2);
        ubicacionService.actualizar(ubicacion3);

        int umbralDeEnrgia = 25;

        List<Ubicacion> ubicacionesSobrecargadas = ubicacionService.ubicacionesSobrecargadas(umbralDeEnrgia);

        assertEquals(2,ubicacionesSobrecargadas.size());
        assertTrue(ubicacionesSobrecargadas.stream()
                .allMatch(u -> u.getEnergia() > umbralDeEnrgia));
    }

    @Test
    public void siSePidenUbicacionesSobreCargadasYNoHayUbicacionesSeDevuelveUnaListaVacia() {
        int umbralDeEnrgia = 25;

        List<Ubicacion> ubicacionesSobrecargadas = ubicacionService.ubicacionesSobrecargadas(umbralDeEnrgia);

        assertTrue(ubicacionesSobrecargadas.isEmpty());
    }

    @Test
    public void siSePideUbicacionesSobreCargadasYNingunaTieneEnergiaMayorAlUmbralSeDevuelveUnaListaVacia() {
        Ubicacion ubicacion1 = this.ubicacionService.crear(ubicacionUno);
        Ubicacion ubicacion2 = this.ubicacionService.crear(ubicacionDos);
        Ubicacion ubicacion3 = this.ubicacionService.crear(ubicacionTres);

        ubicacion1.setEnergia(20);
        ubicacion2.setEnergia(30);
        ubicacion3.setEnergia(40);

        ubicacionService.actualizar(ubicacion1);
        ubicacionService.actualizar(ubicacion2);
        ubicacionService.actualizar(ubicacion3);

        int umbralDeEnrgia = 70;

        List<Ubicacion> ubicacionesSobrecargadas = ubicacionService.ubicacionesSobrecargadas(umbralDeEnrgia);

        assertTrue(ubicacionesSobrecargadas.isEmpty());
    }

    @Test
    void alNoHaberUbicacionesEnNeoIncomingDegreeDevuelveVacio(){
        List<Long> ids = new ArrayList<>();
        List<DegreeResult> results = ubicacionService.incomingDegreeOf(ids);
        assertTrue(results.isEmpty());
    }

    @Test
    void alNoHaberUbicacionesEnNeoOutgoingDegreeDevuelveVacio(){
        List<Long> ids = new ArrayList<>();
        List<DegreeResult> results = ubicacionService.outgoingDegreeOf(ids);
        assertTrue(results.isEmpty());
    }

    @Test
    void alUsarUbicacionesInexistentesIncomingDegreeDevuelveVacio(){
        List<Long> ids = Arrays.asList(1L,2L,3L);
        List<DegreeResult> results = ubicacionService.incomingDegreeOf(ids);
        assertTrue(results.isEmpty());
    }

    @Test
    void alUsarUbicacionesInexistentesOutgoingDegreeDevuelveVacio(){
        List<Long> ids = Arrays.asList(1L,2L,3L);
        List<DegreeResult> results = ubicacionService.outgoingDegreeOf(ids);
        assertTrue(results.isEmpty());
    }

    @Test
    void alHaberUbicacionesEnNeoPeroNoPedirNingunaEnDegreeIncomingDevuelveVacio(){
        this.ubicacionService.crear(this.ubicacionUno);
        this.ubicacionService.crear(this.ubicacionDos);
        this.ubicacionService.crear(this.ubicacionTres);
        this.ubicacionService.conectar(this.ubicacionUno.getId(), this.ubicacionDos.getId());
        this.ubicacionService.conectar(this.ubicacionUno.getId(),this.ubicacionTres.getId());
        List<Long> ids = new ArrayList<>();
        List<DegreeResult> results = ubicacionService.incomingDegreeOf(ids);
        assertTrue(results.isEmpty());
    }

    @Test
    void alHaberUbicacionesEnNeoPeroNoPedirNingunaEnDegreeOutgoingDevuelveVacio(){
        this.ubicacionService.crear(this.ubicacionUno);
        this.ubicacionService.crear(this.ubicacionDos);
        this.ubicacionService.crear(this.ubicacionTres);
        this.ubicacionService.conectar(this.ubicacionUno.getId(), this.ubicacionDos.getId());
        this.ubicacionService.conectar(this.ubicacionUno.getId(),this.ubicacionTres.getId());
        List<Long> ids = new ArrayList<>();
        List<DegreeResult> results = ubicacionService.outgoingDegreeOf(ids);
        assertTrue(results.isEmpty());
    }

    @Test
    void elCalculoDeDegreeCentralityIncomingVariaSiVariosSeConectan(){
        //INCOMING
        this.ubicacionService.crear(this.ubicacionUno);
        this.ubicacionService.crear(this.ubicacionDos);
        this.ubicacionService.crear(this.ubicacionTres);

        this.ubicacionService.conectar(this.ubicacionDos.getId(), this.ubicacionUno.getId());
        this.ubicacionService.conectar(this.ubicacionUno.getId(), this.ubicacionDos.getId());
        this.ubicacionService.conectar(this.ubicacionTres.getId(),ubicacionUno.getId());
        this.ubicacionService.conectar(this.ubicacionTres.getId(),ubicacionDos.getId());

        List<Ubicacion> ubicaciones = ubicacionService.recuperarTodos();
        List<DegreeResult> results = ubicacionService.incomingDegreeOf(ubicaciones.stream().map(Ubicacion::getId).toList());

        // el primero es ubicacionUno o ubicacionDos por tener más nodos apuntándole
        assertTrue(Objects.equals(ubicacionDos.getId(), results.getFirst().ubicacion().getId())
                || Objects.equals(ubicacionUno.getId(), results.getFirst().ubicacion().getId()));
        assertTrue(Objects.equals(ubicacionUno.getId(), results.get(1).ubicacion().getId())
                || Objects.equals(ubicacionDos.getId(), results.get(1).ubicacion().getId()));
        assertEquals(ubicacionTres.getId(), results.get(2).ubicacion().getId());

        assertEquals(1,results.getFirst().degree()); // hay 3 nodos, tiene conectado 2 nodos => 2 / 3 -1 => 2 / 2 = 1
        assertEquals(1,results.get(1).degree());   // hay 3 nodos, tiene conectado 2 nodos => 2 / 3 -1 => 1
        assertEquals(0,results.get(2).degree());   // hay 3 nodos, tiene conectado 0 nodos => 0 / 3 -1 => 0


    }

    @Test
    void elCalculoDeDegreeCentralityOutgoingVariaSiVariosSeConectan(){
        //OUTGOING
        this.ubicacionService.crear(this.ubicacionUno);
        this.ubicacionService.crear(this.ubicacionDos);
        this.ubicacionService.crear(this.ubicacionTres);

        this.ubicacionService.conectar(this.ubicacionDos.getId(), this.ubicacionUno.getId());
        this.ubicacionService.conectar(this.ubicacionUno.getId(), this.ubicacionDos.getId());
        this.ubicacionService.conectar(this.ubicacionTres.getId(),ubicacionUno.getId());
        this.ubicacionService.conectar(this.ubicacionTres.getId(),ubicacionDos.getId());

        List<Ubicacion> ubicaciones = ubicacionService.recuperarTodos();
        List<DegreeResult> results = ubicacionService.outgoingDegreeOf(ubicaciones.stream().map(Ubicacion::getId).toList());


        // el primero es ubicacionTres por apuntar a más nodos
        assertEquals(ubicacionTres.getId(), results.getFirst().ubicacion().getId());
        assertTrue(Objects.equals(ubicacionDos.getId(), results.get(1).ubicacion().getId())
                || Objects.equals(ubicacionUno.getId(), results.get(1).ubicacion().getId()));
        assertTrue(Objects.equals(ubicacionUno.getId(), results.get(2).ubicacion().getId())
                || Objects.equals(ubicacionDos.getId(), results.get(2).ubicacion().getId()));

        assertEquals(1 ,results.getFirst().degree()); // hay 3 nodos, conecta con 2 nodos => 2 / 3 -1 => 2 / 2 = 1
        assertEquals(0.5,results.get(1).degree());   // hay 3 nodos, conecta 1 nodo => 1 / 3 - 1 => 0.5
        assertEquals(0.5,results.get(2).degree());   // hay 3 nodos, conecta 1 nodo => 1 / 3 -1 => 0.5
    }

    @Test
    void elCalculoDeDegreeCentralityIncomingEsCorrectoConVariosNodos(){
        //INCOMING
        this.ubicacionService.crear(this.ubicacionUno);
        this.ubicacionService.crear(this.ubicacionDos);
        this.ubicacionService.crear(this.ubicacionTres);

        this.ubicacionService.conectar(this.ubicacionDos.getId(), this.ubicacionUno.getId());
        this.ubicacionService.conectar(this.ubicacionTres.getId(),ubicacionUno.getId());

        List<Ubicacion> ubicaciones = ubicacionService.recuperarTodos();
        List<DegreeResult> results = ubicacionService.incomingDegreeOf(ubicaciones.stream().map(Ubicacion::getId).toList());

        assertEquals(ubicacionUno.getId(), results.getFirst().ubicacion().getId());
        assertTrue(Objects.equals(ubicacionDos.getId(), results.get(1).ubicacion().getId())
                || Objects.equals(ubicacionTres.getId(), results.get(1).ubicacion().getId()));
        assertTrue(Objects.equals(ubicacionTres.getId(), results.get(2).ubicacion().getId())
                || Objects.equals(ubicacionDos.getId(), results.get(2).ubicacion().getId()));

        assertEquals(1,results.getFirst().degree()); // hay 3 nodos, tiene conectado 2 entrantes nodos => 2 / 3 -1 => 2 / 2 = 1
        assertEquals(0,results.get(1).degree()); // hay 3 nodos, tiene conectado 0 entrantes nodos => 0 / 3 -1 => 0
        assertEquals(0,results.get(2).degree());// hay 3 nodos, tiene conectado 0 entrantes nodos => 0 / 3 -1 => 0
    }

    @Test
    void elCalculoDeDegreeCentralityOutgoingEsCorrectoConVariosNodos(){
        // OUTGOING
        this.ubicacionService.crear(this.ubicacionUno);
        this.ubicacionService.crear(this.ubicacionDos);
        this.ubicacionService.crear(this.ubicacionTres);

        this.ubicacionService.conectar(this.ubicacionUno.getId(), this.ubicacionDos.getId());
        this.ubicacionService.conectar(this.ubicacionUno.getId(),this.ubicacionTres.getId());

        List<Ubicacion> ubicaciones = ubicacionService.recuperarTodos();
        List<DegreeResult> results = ubicacionService.outgoingDegreeOf(ubicaciones.
                stream().
                map(Ubicacion::getId).toList());

        assertEquals(ubicacionUno.getId(), results.getFirst().ubicacion().getId());
        assertTrue(Objects.equals(ubicacionDos.getId(), results.get(1).ubicacion().getId())
                || Objects.equals(ubicacionTres.getId(), results.get(1).ubicacion().getId()));
        assertTrue(Objects.equals(ubicacionTres.getId(), results.get(2).ubicacion().getId())
                || Objects.equals(ubicacionDos.getId(), results.get(2).ubicacion().getId()));

        assertEquals(1,results.getFirst().degree());// hay 3 nodos, el mayor tiene conectado 2 salientes nodos => 2 / 3 -1 => 2 / 2 = 1
        assertEquals(0.0,results.get(1).degree());  // hay 3 nodos, el segundo tiene conectado 0 salientes nodos => 0 / 3 -1 => 0
        assertEquals(0.0,results.get(2).degree());  // hay 3 nodos, el tercero tiene conectado 0 salientes nodos => 0 / 3 -1 => 0

    }


    @Test
    void alCargarMuchosDatosAleatoriosConConexionesAlgunoTieneOutgoingDegreeMayorACero() throws IOException {
        ClassPathResource nodeGenPath = new ClassPathResource("cypher/nodeGenerator.cypher");
        ClassPathResource nodeConPath = new ClassPathResource("cypher/randomConnect.cypher");

        String cypherNodeGen = new String(nodeGenPath.getInputStream().readAllBytes());
        String cypherNodeCon = new String(nodeConPath.getInputStream().readAllBytes());

        neo4jClient.query(cypherNodeGen).run();
        neo4jClient.query(cypherNodeCon).run();

        List<Long> ids = LongStream.rangeClosed(1,100).boxed().toList();
        List<DegreeResult> results = ubicacionService.outgoingDegreeOf(ids);

        assertTrue(results.stream().anyMatch(r -> r.degree() > 0.0));

    }

    @Test
    void alCargarMuchosDatosAleatoriosConConexionesAlgunoTieneIncomingDegreeMayorACero() throws IOException {
        ClassPathResource nodeGenPath = new ClassPathResource("cypher/nodeGenerator.cypher");
        ClassPathResource nodeConPath = new ClassPathResource("cypher/randomConnect.cypher");

        String cypherNodeGen = new String(nodeGenPath.getInputStream().readAllBytes());
        String cypherNodeCon = new String(nodeConPath.getInputStream().readAllBytes());

        neo4jClient.query(cypherNodeGen).run();
        neo4jClient.query(cypherNodeCon).run();

        List<Long> ids = LongStream.rangeClosed(1,100).boxed().toList();
        List<DegreeResult> results = ubicacionService.incomingDegreeOf(ids);

        assertTrue(results.stream().anyMatch(r -> r.degree() > 0.0));

    }

    @Test
    void alNoHaberUbicacionesEnNeoClosenessCentralityDevuelveVacio(){
        List<Long> ids = new ArrayList<>();
        List<ClosenessResult> results = ubicacionService.closenessOf(ids);
        assertTrue(results.isEmpty());
    }

    @Test
    void alUsarUbicacionesInexistentesClosenessCentralityDevuelveVacio(){
        List<Long> ids = Arrays.asList(1L,2L,3L);
        List<ClosenessResult> results = ubicacionService.closenessOf(ids);
        assertTrue(results.isEmpty());
    }

    @Test
    void alHaberUbicacionesEnNeoPeroNoPedirNingunaEnClosenessCentralityDevuelveVacio(){
        this.ubicacionService.crear(this.ubicacionUno);
        this.ubicacionService.crear(this.ubicacionDos);
        this.ubicacionService.crear(this.ubicacionTres);

        this.ubicacionService.conectar(this.ubicacionUno.getId(), this.ubicacionDos.getId());
        this.ubicacionService.conectar(this.ubicacionUno.getId(),this.ubicacionTres.getId());

        List<Long> ids = new ArrayList<>();
        List<ClosenessResult> results = ubicacionService.closenessOf(ids);
        assertTrue(results.isEmpty());
    }

    @Test
    public void seObtieneLosResultadosDeAplicarElAlgoritmoDeClosenessCentralityACincoUbicacionesDiferentes() throws IOException {

        ClassPathResource nodeGenPath = new ClassPathResource("cypher/nodeGeneratorOf5.cypher");
        ClassPathResource nodeConPath = new ClassPathResource("cypher/connectOf5.cypher");

        String cypherNodeGen = new String(nodeGenPath.getInputStream().readAllBytes());
        String cypherNodeCon = new String(nodeConPath.getInputStream().readAllBytes());

        neo4jClient.query(cypherNodeGen).run();
        neo4jClient.query(cypherNodeCon).run();

        List<Long> ids = List.of(1L,2L,3L,4L,5L);

        List<ClosenessResult> resultados = this.ubicacionService.closenessOf(ids);

        assertEquals(2, resultados.get(0).ubicacion().getId());

        assertEquals(0.25, resultados.get(0).closeness());
        assertEquals(0.2, resultados.get(1).closeness());
        assertEquals(0.2, resultados.get(2).closeness());
        assertEquals(1.0/6, resultados.get(3).closeness());
        assertEquals(1.0/6, resultados.get(4).closeness());
    }

    @Test
    public void seObtieneLosResultadosDeAplicarElAlgoritmoDeClosenessCentralityAUnaUbicacionConectadaAOtraNoConsiderada() {
        this.ubicacionService.crear(this.ubicacionUno);
        this.ubicacionService.crear(this.ubicacionDos);
        this.ubicacionService.conectar(this.ubicacionUno.getId(), this.ubicacionDos.getId());
        this.ubicacionService.conectar(this.ubicacionDos.getId(),this.ubicacionUno.getId());
        List<Long> ids = List.of(this.ubicacionUno.getId());

        List<ClosenessResult> resultados = this.ubicacionService.closenessOf(ids);

        assertTrue(resultados.isEmpty());
    }

    @Test
    public void seObtieneLosResultadosDeAplicarElAlgoritmoDeClosenessCentrailityAUnaListaDeUbicacionesQueNoExisten() {
        List<Long> ids = List.of(-1L, -2L, -3L, -4L, -5L);

        List<ClosenessResult> resultados = this.ubicacionService.closenessOf(ids);

        assertTrue(resultados.isEmpty());
    }

    @Test
    void alCargarMuchosDatosAleatoriosConConexionesAlgunoTieneClosenessCentralityMayorACero() throws IOException {
        ClassPathResource nodeGenPath = new ClassPathResource("cypher/nodeGenerator.cypher");
        ClassPathResource nodeConPath = new ClassPathResource("cypher/randomConnect.cypher");

        String cypherNodeGen = new String(nodeGenPath.getInputStream().readAllBytes());
        String cypherNodeCon = new String(nodeConPath.getInputStream().readAllBytes());

        neo4jClient.query(cypherNodeGen).run();
        neo4jClient.query(cypherNodeCon).run();

        List<Long> ids = LongStream.rangeClosed(1,100).boxed().toList();
        List<ClosenessResult> results = ubicacionService.closenessOf(ids);

        assertTrue(results.stream().anyMatch(r -> r.closeness() > 0.0));

    }

    @Test
    void alCrearseUnaUbicacionSePersistenLasCoordenadasQueDelimitanSuTerritorio() {
        ubicacionUno.setTerritorio(pointsUbicacionUno);
        ubicacionDos.setTerritorio(pointsUbicacionDos);

        ubicacionService.crear((ubicacionUno));
        ubicacionService.crear((ubicacionDos));

        Ubicacion recuperadaUno = ubicacionService.recuperar(ubicacionUno.getId()).get();
        Ubicacion recuperadaDos = ubicacionService.recuperar(ubicacionDos.getId()).get();

        assertEquals(recuperadaUno.getCoordenadas() , pointsUbicacionUno );
        assertEquals(recuperadaDos.getCoordenadas() , pointsUbicacionDos );

        assertEquals(2, ubicacionService.recuperarTodos().size());

    }

    @Test
    void alCrearseUnaUbicacionConUnAreaQueIntersecteUnaUbicacionExistenteSeLanzaUnaExcepcion() {
        List<Coordenada> areaQueIntersecta = List.of(new Coordenada(-0.5,0.5),new Coordenada(0.5,0.5),
                new Coordenada(0.5,1.5),new Coordenada(-0.5,1.5),new Coordenada(-0.5,0.5));
        ubicacionDos.setTerritorio(areaQueIntersecta);

        ubicacionService.crear(this.ubicacionUno);

        assertThrows(AreaOcupadaException.class, () -> ubicacionService.crear(this.ubicacionDos));
    }

    @Test
    void alCrearUnPoligonoAbiertoSeAgregaElUltimoPuntoIgualAlPrimeroParaCerrarElPoligono() {

        List<Coordenada> coordenadasPoligonoAbierto =
                List.of(new Coordenada(-70,0),new Coordenada(-70,1)
                ,new Coordenada(-80,1));

        ubicacionUno.setTerritorio(coordenadasPoligonoAbierto);
        ubicacionService.crear(this.ubicacionUno);


        Ubicacion ubicacionRecuperada = ubicacionService.recuperar(ubicacionUno.getId()).get();

        assertEquals(4, ubicacionRecuperada.getCoordenadas().size());
        assertEquals(ubicacionRecuperada.getCoordenadas().get(0), coordenadasPoligonoAbierto.get(0));
        assertEquals(ubicacionRecuperada.getCoordenadas().get(1), coordenadasPoligonoAbierto.get(1));
        assertEquals(ubicacionRecuperada.getCoordenadas().get(2), coordenadasPoligonoAbierto.get(2));
        assertEquals(ubicacionRecuperada.getCoordenadas().get(3), coordenadasPoligonoAbierto.get(0));//aca el primero es igual al ultimo
    }

    @Test
    void seCreaUnaUbicacionYSuTemperaturaEsIgualALaTemperaturaInicialRecibida() throws InterruptedException {
        Thread.sleep(35000);
        ubicacionService.crear(ubicacionUno);

        Ubicacion ubicacionRecuperada = ubicacionService.recuperar(ubicacionUno.getId()).get();

        assertEquals(5d, ubicacionRecuperada.getTemperatura());
    }

    @Test
    void seCreaUnaUbicacionYSuTemperaturaDebeSerMayorOIgualAMenos20() throws InterruptedException {
        Thread.sleep(35000);
        ubicacionUno.establecerTemperatura(-30);
        ubicacionService.crear(ubicacionUno);

        Ubicacion ubicacionRecuperada = ubicacionService.recuperar(ubicacionUno.getId()).get();

        assertEquals(-20, ubicacionRecuperada.getTemperatura());
    }

    @Test
    void seCreaUnaUbicacionYSuTemperaturaDebeSerMenorOIgualA50() throws InterruptedException {
        Thread.sleep(35000);
        ubicacionUno.establecerTemperatura(51);
        ubicacionService.crear(ubicacionUno);

        Ubicacion ubicacionRecuperada = ubicacionService.recuperar(ubicacionUno.getId()).get();

        assertEquals(50, ubicacionRecuperada.getTemperatura());
    }

    @Test
    void seCreaUnDemonioYBajaLaTemperaturaDeSuUbicacionEnCeroComa2Grados() throws InterruptedException {
        Thread.sleep(35000);
        ubicacionService.crear(ubicacionUno);

        double tempInicial = ubicacionUno.getTemperatura();

        espirituService.crear(demonioUno); // se crea en "Bernal"

        Ubicacion ubicacionRecuperada = ubicacionService.recuperar(ubicacionUno.getId()).get();

        assertTrue(tempInicial > ubicacionRecuperada.getTemperatura());
        assertEquals(tempInicial - 0.2, ubicacionRecuperada.getTemperatura());
    }

    @Test
    void seCreanDemoniosYBajaLaTemperaturaDeSuUbicacionEnCeroComa2GradosPorCada() throws InterruptedException {
        Thread.sleep(35000);
        ubicacionService.crear(ubicacionUno);

        double tempInicial = ubicacionUno.getTemperatura();

        espirituService.crear(demonioUno); // se crea en "Bernal"
        espirituService.crear(demonioDos); // se crea en "Bernal"

        Ubicacion ubicacionRecuperada = ubicacionService.recuperar(ubicacionUno.getId()).get();

        assertTrue(tempInicial > ubicacionRecuperada.getTemperatura());
        assertEquals(tempInicial - 0.4, ubicacionRecuperada.getTemperatura());
    }

    // TODO ángeles impl y tests

    @Test
    void seMueveUnDemonioMedianteUnaInvocacionYRevierteLaTemperaturaDeSuUbicacionAnterior() throws InterruptedException {
        Thread.sleep(35000);

        mediumUno.setCoordenada(new Coordenada(30.11, 30.11));
        demonioUno.setCoordenada(new Coordenada(29.99, 29.99));

        double tempInicial = santuarioUno.getTemperatura();

        ubicacionService.crear(cementerioDos);
        ubicacionService.crear(santuarioUno);
        mediumService.crear(mediumUno); // está en un cementerio
        espirituService.crear(demonioUno); // está en un santuario

        mediumService.invocar(mediumUno.getId(), demonioUno.getId()); // se mueve hacia el cementerio

        Santuario santuarioRecuperado = (Santuario) ubicacionService.recuperar(santuarioUno.getId()).get();

        assertEquals(tempInicial, santuarioRecuperado.getTemperatura());
    }

    @Test
    void seMueveUnDemonioMedianteUnaInvocacionYBajaLaTemperaturaDeSuUbicacionActual() throws InterruptedException {
        Thread.sleep(35000);

        mediumUno.setCoordenada(new Coordenada(30.11, 30.11));
        demonioUno.setCoordenada(new Coordenada(29.99, 29.99));

        double tempInicial = cementerioDos.getTemperatura();

        ubicacionService.crear(cementerioDos);
        ubicacionService.crear(santuarioUno);
        mediumService.crear(mediumUno); // está en un cementerio
        espirituService.crear(demonioUno); // está en un santuario

        mediumService.invocar(mediumUno.getId(), demonioUno.getId()); // se mueve hacia el cementerio

        Cementerio cementerioRecuperado = (Cementerio) ubicacionService.recuperar(cementerioDos.getId()).get();

        assertTrue(tempInicial > cementerioRecuperado.getTemperatura());
        assertEquals(tempInicial - 0.2, cementerioRecuperado.getTemperatura());
    }

    @Test
    void seMueveUnDemonioMedianteElMovimientoDeUnMediumConectadoYBajaLaTemperaturaDeSuUbicacionActual() throws InterruptedException {
        Thread.sleep(35000);

        mediumUno.setCoordenada(new Coordenada(29.99, 29.99));
        demonioUno.setCoordenada(new Coordenada(29.99, 29.99));

        double tempInicial = cementerioDos.getTemperatura();

        ubicacionService.crear(cementerioDos);
        ubicacionService.crear(santuarioUno);
        mediumService.crear(mediumUno); // está en un cementerio
        espirituService.crear(demonioUno); // está en un santuario

        ubicacionService.conectar(santuarioUno.getId(), cementerioDos.getId());

        espirituService.conectar(demonioUno.getId(), mediumUno.getId());
        mediumService.mover(mediumUno.getId(), 30.11, 30.11); // hacia el cementerio

        Cementerio cementerioRecuperado = (Cementerio) ubicacionService.recuperar(cementerioDos.getId()).get();

        assertTrue(tempInicial > cementerioRecuperado.getTemperatura());
        assertEquals(tempInicial - 0.2, cementerioRecuperado.getTemperatura());
    }

    @Test
    void seMueveUnDemonioMedianteElMovimientoDeUnMediumConectadoYSeReestableceLaTemperaturaDeSuUbicacionAnterior() throws InterruptedException {
        Thread.sleep(35000);

        mediumUno.setCoordenada(new Coordenada(29.99, 29.99));
        demonioUno.setCoordenada(new Coordenada(29.99, 29.99));

        double tempInicial = santuarioUno.getTemperatura();

        ubicacionService.crear(cementerioDos);
        ubicacionService.crear(santuarioUno);
        mediumService.crear(mediumUno); // está en un cementerio
        espirituService.crear(demonioUno); // está en un santuario

        ubicacionService.conectar(santuarioUno.getId(), cementerioDos.getId());

        espirituService.conectar(demonioUno.getId(), mediumUno.getId());
        mediumService.mover(mediumUno.getId(), 30.11, 30.11); // hacia el cementerio

        Santuario santuarioRecuperado = (Santuario) ubicacionService.recuperar(santuarioUno.getId()).get();

        assertEquals(tempInicial, santuarioRecuperado.getTemperatura());
    }

//    @Test
//    void seGuardaUnaLecturaDeTemperaturaYApareceEnLaBaseDeDatos() {
//        ubicacionService.crear(ubicacionUno);
//
//        ubicacionRepository.cambiarTemperatura(ubicacionUno.getId(), 15d);
//
//        double temperaturaUbicacion = ubicacionRepository.getTemperatura(ubicacionUno.getId());
//
//        assertEquals(15d, temperaturaUbicacion);
//    }
//
//    @Test
//    void seGuardaUnaLecturaDeTemperaturaYSeLaConsideraFria() {
//        ubicacionService.crear(ubicacionUno);
//
//        ubicacionRepository.cambiarTemperatura(ubicacionUno.getId(), 5);
//
//        boolean esFria = ubicacionRepository.tieneDeltaDeTemperaturaMenorOIgual(ubicacionUno.getId(), -5);
//
//        assertTrue(esFria);
//    }
//
//    @Test
//    void seGuardanLecturasDeTemperaturaYSeLasConsideraFrias() {
//        ubicacionService.crear(ubicacionUno);
//        ubicacionService.crear(ubicacionDos);
//
//        ubicacionRepository.cambiarTemperatura(ubicacionUno.getId(), 5);
//        ubicacionRepository.cambiarTemperatura(ubicacionDos.getId(), 5);
//
//        boolean esFria1 = ubicacionRepository.tieneDeltaDeTemperaturaMenorOIgual(ubicacionUno.getId(), -5);
//        boolean esFria2 = ubicacionRepository.tieneDeltaDeTemperaturaMenorOIgual(ubicacionDos.getId(), -5);
//
//        assertTrue(esFria1);
//        assertTrue(esFria2);
//    }
//
//    @Test
//    void seGuardaUnaLecturaDeTemperaturaYSeLaConsideraNormal() {
//        ubicacionService.crear(ubicacionUno);
//
//        ubicacionRepository.cambiarTemperatura(ubicacionUno.getId(), 20);
//
//        boolean esFria = ubicacionRepository.tieneDeltaDeTemperaturaMenorOIgual(ubicacionUno.getId(), -5);
//
//        assertFalse(esFria);
//    }
//
//    @Test
//    void seGuardanLecturasDeTemperaturaYSeLasConsideraNormales() {
//        ubicacionService.crear(ubicacionUno);
//        ubicacionService.crear(ubicacionDos);
//
//        ubicacionRepository.cambiarTemperatura(ubicacionUno.getId(), 20);
//        ubicacionRepository.cambiarTemperatura(ubicacionDos.getId(), 23);
//
//        boolean esFria1 = ubicacionRepository.tieneDeltaDeTemperaturaMenorOIgual(ubicacionUno.getId(), -5);
//        boolean esFria2 = ubicacionRepository.tieneDeltaDeTemperaturaMenorOIgual(ubicacionDos.getId(), -5);
//
//        assertFalse(esFria1);
//        assertFalse(esFria2);
//    }
//
//    @Test
//    void seGuardanLecturasDeTemperaturaMixtaYSeLasConsideraComoCorresponda() {
//        ubicacionService.crear(ubicacionUno);
//        ubicacionService.crear(ubicacionDos);
//
//        ubicacionRepository.cambiarTemperatura(ubicacionUno.getId(), 5);
//        ubicacionRepository.cambiarTemperatura(ubicacionDos.getId(), 23);
//
//        boolean esFria1 = ubicacionRepository.tieneDeltaDeTemperaturaMenorOIgual(ubicacionUno.getId(), -5);
//        boolean esFria2 = ubicacionRepository.tieneDeltaDeTemperaturaMenorOIgual(ubicacionDos.getId(), -5);
//
//        assertTrue(esFria1);
//        assertFalse(esFria2);
//    }

    @Test
    void enLaUbicacionMasPeligrosaSinUbicacionesRetornaOptionalVacio(){

        Optional<Ubicacion> ubicacionOptional = ubicacionService.ubicacionMasPeligrosa();
        assertTrue(ubicacionOptional.isEmpty());
    }

    @Test
    void seRecuperaLaUbicacionMasPeligrosaCuandoHaySoloUnaYDevuelveEsa(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.ubicacionService.crear(ubicacionUno);

        Long id = this.ubicacionUno.getId();

//        Point lecturaPoint1 = Point.measurement("temperaturas")
//                .addTag("ubicacion", idString)
//                .addField("temp", 40)
//                .time(Instant.now().toEpochMilli(), WritePrecision.MS);
//
//        ubicacionDAOINFLUX.guardarDatos(lecturaPoint1);

        ubicacionRepository.setTemperatura(id, 40);

        Ubicacion ubicacionRecuperada = ubicacionService.ubicacionMasPeligrosa().get();

        assertEquals(id,ubicacionRecuperada.getId());
    }


    @Test
    void seRecuperaLaUbicacionMasPeligrosaEnBaseAVariasLecturas() throws InterruptedException {
        Thread.sleep(11000);
        this.ubicacionService.crear(ubicacionUno); // TODO cambiar para que reciba temp ambiente
        this.ubicacionService.crear(ubicacionDos);
        this.ubicacionService.crear(ubicacionTres);

        Long id = this.ubicacionUno.getId();

        Long id2 = this.ubicacionDos.getId();

        Long id3 = this.ubicacionTres.getId();

//        Point lecturaPoint0 = Point.measurement("temperaturas")
//                .addTag("ubicacion", "10")
//                .addField("temp", 50)
//                .time(Instant.now().toEpochMilli(), WritePrecision.MS);
//        Point lecturaPoint1 = Point.measurement("temperaturas")
//                .addTag("ubicacion", idString)
//                .addField("temp", 40)
//                .time(Instant.now().toEpochMilli(), WritePrecision.MS);
//        Point lecturaPoint2 = Point.measurement("temperaturas")
//                .addTag("ubicacion", idString2)
//                .addField("temp", 0)
//                .time(Instant.now().toEpochMilli(), WritePrecision.MS);
//        Point lecturaPoint3 = Point.measurement("temperaturas")
//                .addTag("ubicacion", idString)
//                .addField("temp", 10)
//                .time(Instant.now().toEpochMilli(), WritePrecision.MS);

        ubicacionRepository.setTemperatura(id, 50);
        ubicacionRepository.setTemperatura(id3, 40);
        ubicacionRepository.setTemperatura(id2, 0);
        ubicacionRepository.setTemperatura(id, 10);

//        ubicacionDAOINFLUX.guardarDatos(lecturaPoint0);
//        ubicacionDAOINFLUX.guardarDatos(lecturaPoint1);
//        ubicacionDAOINFLUX.guardarDatos(lecturaPoint2);
//        ubicacionDAOINFLUX.guardarDatos(lecturaPoint3);

        Ubicacion ubicacionRecuperada = ubicacionService.ubicacionMasPeligrosa().get();

        assertEquals(id2,ubicacionRecuperada.getId());
    }

    @Test
    public void cuandoNoHayUbicacionesNoHayUbicacionConMasTransitoRetornaOptionalVacio() throws InterruptedException {
        Thread.sleep(11000);

        Optional<Ubicacion> ubicacionOptional = ubicacionService.ubicacionConMasTransitoDeEspiritus();
        assertTrue(ubicacionOptional.isEmpty());
    }

    @Test
    public void seRecuperaLaUnicaUbicacionQueHayParaMayorTransitoDeEspiritus() throws InterruptedException {
        Thread.sleep(11000);
        this.ubicacionService.crear(ubicacionUno);

//        Long id = this.ubicacionUno.getId();
//        String idString = id.toString();

//        ubicacionDAOINFLUX.guardarDatos(Point.measurement("temperaturas")
//                .addTag("ubicacion", idString)
//                .addField("temp", 50.1)
//                .time(fechaFija(1, 0).toEpochMilli(), WritePrecision.MS));
//
//        ubicacionDAOINFLUX.guardarDatos(Point.measurement("temperaturas")
//                .addTag("ubicacion", idString)
//                .addField("temp", 55.1)
//                .time(fechaFija(1, 5).toEpochMilli(), WritePrecision.MS));

        ubicacionRepository.setTemperatura(ubicacionUno.getId(),50.1);
        try {
            Thread.sleep(1000); // para que no se solapen las escrituras
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ubicacionRepository.setTemperatura(ubicacionUno.getId(), 55.1);

        Ubicacion ubicacionRecuperada = ubicacionService.ubicacionConMasTransitoDeEspiritus().get();

        assertEquals(ubicacionUno.getId(), ubicacionRecuperada.getId());
    }

    @Test
    public void seRecuperaLaUbicacionQueTuvoLaMayorCantidadDeVariacionesDeTemperaturaConsecutivas() throws InterruptedException {
        Thread.sleep(11000);
        // los thread sleep son para darle tiempo a la db de salir de los tests anteriores y para que no se pisen

        this.ubicacionService.crear(ubicacionUno);
        this.ubicacionService.crear(ubicacionDos);

        Long id1 = this.ubicacionUno.getId();
        String idString1 = id1.toString();

        Long id2 = this.ubicacionDos.getId();
        String idString2 = id2.toString();

//        ubicacionDAOINFLUX.guardarDatos(Point.measurement("temperaturas")
//                .addTag("ubicacion", idString1)
//                .addField("temp", 20.1)
//                .time(fechaFija(2, 0).toEpochMilli(), WritePrecision.MS));
//        ubicacionDAOINFLUX.guardarDatos(Point.measurement("temperaturas")
//                .addTag("ubicacion", idString1)
//                .addField("temp", 20.1)
//                .time(fechaFija(2, 5).toEpochMilli(), WritePrecision.MS));
//        ubicacionDAOINFLUX.guardarDatos(Point.measurement("temperaturas")
//                .addTag("ubicacion", idString1)
//                .addField("temp", 25.1)
//                .time(fechaFija(2, 10).toEpochMilli(), WritePrecision.MS));

        ubicacionRepository.setTemperatura(ubicacionUno.getId(),20.1);
        try {
            Thread.sleep(750);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ubicacionRepository.setTemperatura(ubicacionUno.getId(),20.1);
        try {
            Thread.sleep(750);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ubicacionRepository.setTemperatura(ubicacionUno.getId(), 25.1);
        try {
            Thread.sleep(750);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        ubicacionRepository.setTemperatura(ubicacionDos.getId(),30.1);
        try {
            Thread.sleep(750);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ubicacionRepository.setTemperatura(ubicacionDos.getId(), 35.1);
        try {
            Thread.sleep(750);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ubicacionRepository.setTemperatura(ubicacionDos.getId(), 32.1);

//        ubicacionDAOINFLUX.guardarDatos(Point.measurement("temperaturas")
//                .addTag("ubicacion", idString2)
//                .addField("temp", 30.1)
//                .time(fechaFija(2, 1).toEpochMilli(), WritePrecision.MS));
//        ubicacionDAOINFLUX.guardarDatos(Point.measurement("temperaturas")
//                .addTag("ubicacion", idString2)
//                .addField("temp", 35.1)
//                .time(fechaFija(2, 6).toEpochMilli(), WritePrecision.MS));
//        ubicacionDAOINFLUX.guardarDatos(Point.measurement("temperaturas")
//                .addTag("ubicacion", idString2)
//                .addField("temp", 32.1)
//                .time(fechaFija(2, 11).toEpochMilli(), WritePrecision.MS));

        Ubicacion ubicacionRecuperada = ubicacionService.ubicacionConMasTransitoDeEspiritus().get();

        assertEquals(id2, ubicacionRecuperada.getId());
    }


    @Test
    public void cuandoNoHayEventosDePortalesOcurridosDaListaVacia(){

        List<EventoPortalDTO> eventosPortal = ubicacionService.eventosDePortalesOcurridos();
        assertTrue(eventosPortal.isEmpty());
    }

    @Test
    public void seConsiguenLosEventosDePortalesOcurridosConUnSoloPortalYUnaUbicacion() {
        ubicacionUno.setTemperatura(20);
        this.ubicacionService.crear(ubicacionUno);
        espirituService.crear(demonioUno);

        Long id1 = this.ubicacionUno.getId();

        ubicacionRepository.setTemperatura(ubicacionUno.getId(),17.8);
        try {
            Thread.sleep(750);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        espirituService.crearPortal(new PortalDemonios(new Coordenada(0.5, 0.5)));

        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<EventoPortalDTO> eventosPortal = ubicacionService.eventosDePortalesOcurridos();

        assertEquals(1,eventosPortal.size());

        EventoPortalDTO evento = eventosPortal.get(0);

        assertEquals(id1, evento.getUbicacionId());

        assertTrue(evento.getIntensidad() == -5);

    }

    @Test
    public void seConsiguenLosEventosDePortalesOcurridosCon2PortalesOcurridosEnDistintasUbicacion() {
        ubicacionUno.setTemperatura(20);
        ubicacionDos.setTemperatura(20);
        this.ubicacionService.crear(ubicacionUno);
        this.ubicacionService.crear(ubicacionDos);
        demonioUno.setCoordenada(new Coordenada(3.5, 3.5));
        espirituService.crear(demonioUno);
        espirituService.crear(demonioDos);

        Long id1 = this.ubicacionUno.getId();
        Long id2 = this.ubicacionDos.getId();

        ubicacionRepository.setTemperatura(ubicacionUno.getId(),17.8);
        try {
            Thread.sleep(750);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ubicacionRepository.setTemperatura(ubicacionDos.getId(),17.8);
        try {
            Thread.sleep(750);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        espirituService.crearPortal(new PortalDemonios(new Coordenada(0.5, 0.5)));

        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        espirituService.crearPortal(new PortalDemonios(new Coordenada(3.5, 3.5)));

        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<EventoPortalDTO> eventosPortal = ubicacionService.eventosDePortalesOcurridos();

        assertEquals(2,eventosPortal.size());

        EventoPortalDTO evento1 = eventosPortal.get(0);
        EventoPortalDTO evento2 = eventosPortal.get(0);

        assertEquals(id1, evento1.getUbicacionId());
        assertEquals(id2, evento2.getUbicacionId());

        assertTrue(evento1.getIntensidad() == -5);
        assertTrue(evento2.getIntensidad() == -5);

    }

    @Test
    public void seConsiguenLosEventosDePortalesOcurridosCon2PortalesOcurridosEnLaMismaUbicacion() {
        ubicacionUno.setTemperatura(20);
        this.ubicacionService.crear(ubicacionUno);
        espirituService.crear(demonioUno);

        Long id1 = this.ubicacionUno.getId();

        ubicacionRepository.setTemperatura(ubicacionUno.getId(),17.8);
        try {
            Thread.sleep(750);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        espirituService.crearPortal(new PortalDemonios(new Coordenada(0.5, 0.5)));

        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        espirituService.crearPortal(new PortalDemonios(new Coordenada(0.5, 0.5)));

        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<EventoPortalDTO> eventosPortal = ubicacionService.eventosDePortalesOcurridos();

        assertEquals(2,eventosPortal.size());

        EventoPortalDTO evento1 = eventosPortal.get(0);
        EventoPortalDTO evento2 = eventosPortal.get(1);

        assertEquals(id1, evento1.getUbicacionId());
        assertEquals(id1, evento2.getUbicacionId());

        assertTrue(evento1.getIntensidad() == -5);
        assertTrue(evento2.getIntensidad() == -5);

    }

    @Test
    public void laTemperaturaNoPuedeSerMenorAMenosVeinte() {
        ubicacionUno.setTemperatura(-21);
        ubicacionService.crear(ubicacionUno);
        Ubicacion ubicacion = ubicacionService.recuperar(ubicacionUno.getId()).get();
        assertEquals(-20, ubicacion.getTemperatura());
    }

    @Test
    public void laTemperaturaNoPuedeSerMayorACincuenta() {
        ubicacionUno.setTemperatura(51);
        ubicacionService.crear(ubicacionUno);
        Ubicacion ubicacion = ubicacionService.recuperar(ubicacionUno.getId()).get();
        assertEquals(50, ubicacion.getTemperatura());
    }

    @AfterEach
    public void tearDown() {
        this.limpiador.eliminarEspiritus();
        this.limpiador.eliminarMediums();
        this.limpiador.eliminarUbicaciones();
    }
}

