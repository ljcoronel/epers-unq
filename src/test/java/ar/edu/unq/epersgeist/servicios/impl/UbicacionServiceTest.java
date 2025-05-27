package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.exception.EntidadInexistenteException;
import ar.edu.unq.epersgeist.modelo.exception.NombreRepetidoException;
import ar.edu.unq.epersgeist.modelo.exception.EntidadTodaviaReferenciadaException;
import ar.edu.unq.epersgeist.modelo.exception.UbicacionesNoConectadasException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.persistencia.ClosenessResult;
import ar.edu.unq.epersgeist.persistencia.DegreeResult;
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
import java.util.*;
import java.util.stream.LongStream;

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

    private Ubicacion ubicacionUno;
    private Ubicacion ubicacionDos;
    private Ubicacion ubicacionTres;

    private Santuario ubicacionCuatro;
    private Santuario ubicacionCinco;

    private Angel angelUno;

    private Demonio demonioUno;

    private Medium mediumUno;
    private Medium mediumDos;
    private Medium mediumTres;

    @Autowired
    private TestService limpiador;

    @Autowired
    private Neo4jClient neo4jClient;

    @BeforeEach
    void prepare() {
        this.ubicacionUno = new Santuario("Bernal", 50);
        this.ubicacionDos = new Santuario("Quilmes", 50);
        this.ubicacionTres = new Santuario("Avellaneda", 50);

        this.angelUno = new Angel(10, "angelical", this.ubicacionUno);

        this.demonioUno = new Demonio(10, "demonio", this.ubicacionUno);

        this.mediumUno = new Medium("Fabian", 100, 10, this.ubicacionUno);
        this.mediumDos = new Medium("Franco", 200, 50, this.ubicacionUno);
        this.mediumTres = new Medium("Tobias", 300, 60, this.ubicacionUno);

        this.ubicacionCuatro = new Santuario("Chocolandia", 75);
        this.ubicacionCinco = new Santuario("MiCasa", 30);
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

        assertEquals(ubicaciones.getFirst().getId(), this.ubicacionUno.getId());
        assertEquals(ubicaciones.get(1).getId(), this.ubicacionDos.getId());
        assertEquals(ubicaciones.getLast().getId(), this.ubicacionTres.getId());
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

        this.mediumService.mover(mediumUno.getId(), ubicacionUno.getId());
        this.mediumService.mover(mediumDos.getId(), ubicacionUno.getId());
        this.mediumService.mover(mediumTres.getId(), ubicacionUno.getId());

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
        Ubicacion ubicacion1 = new Santuario("ElClerigo",50);
        Ubicacion ubicacion2 = new Santuario("Masedonia",50);
        ubicacionService.crear(ubicacion1);
        ubicacionService.crear(ubicacion2);
        Medium medium = new Medium("Franco", 200, 50, ubicacion1);
        Espiritu espiritu = new Angel(10, "angelical", ubicacion1);
        espirituService.crear(espiritu);
        mediumService.crear(medium);

        espirituService.conectar(espiritu.getId(), medium.getId());
        espiritu.mover(ubicacion2);

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
    public void unaUbicacionEstaConectadaConsigoMisma() {
        Ubicacion ubicacion1 = this.ubicacionService.crear(ubicacionUno);

        assertTrue(ubicacionService.estanConectadas(ubicacion1.getId(), ubicacion1.getId()));
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
    public void alBuscarElCaminoMasCortoEntreDosUbicacionesQueNoEstanConectadasDeNingunaFormaRetornaUnaExepcion(){
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
    public void alBuscarElCaminoMasCortoConUnaUBicacionOrigenQueNoExisteRetornaNull(){
        Ubicacion ubicacion1 = this.ubicacionService.crear(ubicacionUno);

        assertNull(ubicacionService.caminoMasCorto(-1L,ubicacion1.getId()));

    }

    @Test
    public void alBuscarElCaminoMasCortoConUnaUBicacionDestinoQueNoExisteRetornaExeption(){
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

    @AfterEach
    public void tearDown() {
        this.limpiador.eliminarEspiritus();
        this.limpiador.eliminarMediums();
        this.limpiador.eliminarUbicaciones();
    }
}

