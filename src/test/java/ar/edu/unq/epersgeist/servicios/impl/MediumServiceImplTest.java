package ar.edu.unq.epersgeist.servicios.impl;
import ar.edu.unq.epersgeist.modelo.geo.Coordenada;
import ar.edu.unq.epersgeist.modelo.exception.EntidadInexistenteException;
import ar.edu.unq.epersgeist.modelo.exception.EntidadTodaviaReferenciadaException;
import ar.edu.unq.epersgeist.modelo.exception.espiritu.EspirituMuyLejanoException;
import ar.edu.unq.epersgeist.modelo.exception.medium.MediumEnDistintaUbicacionException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.servicios.exception.UbicacionLejanaException;
import ar.edu.unq.epersgeist.testUtils.TestService;
import ar.edu.unq.epersgeist.testUtils.FakeRandomStrategyImpl;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.RandomGenerator.RandomGenerator;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.exception.medium.ExorcistaSinAngelesException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MediumServiceImplTest {

    @Autowired
    private MediumServiceImpl mediumService;
    @Autowired
    private TestService testService;
    @Autowired
    private EspirituServiceImpl espirituService;
    @Autowired
    private UbicacionServiceImpl ubicacionService;

    private Medium medium;
    private Medium medium2;
    private Medium medium3;

    private Angel angelDebil;
    private Angel angelDebil2;
    private Angel angelFuerte;
    private Angel angelEnCementerio;

    private Demonio demonioDebil;
    private Demonio demonioDebil2;
    private Demonio demonioFuerte;
    private Demonio demonioEnCementerio;

    private Ubicacion santuario;
    private Ubicacion santuario2;
    private Ubicacion cementerio;
    private Ubicacion cementerio2;
    private Ubicacion ubicacion2;
    private Ubicacion ubicacionNoConectada;

    private FakeRandomStrategyImpl fakeRandomStrategy;
    private Santuario santuario3;


    @BeforeEach
    void crearMedium(){
        this.santuario = new Santuario("Bernal", 50, List.of(
                new Coordenada(0, 0),
                new Coordenada(1, 0),
                new Coordenada(1, 1),
                new Coordenada(0, 1),
                new Coordenada(0, 0)
        ),5);
        this.santuario2 = new Santuario("Villa Devoto", 90,List.of(
                new Coordenada(3, 0),
                new Coordenada(4, 0),
                new Coordenada(4, 1),
                new Coordenada(3, 1),
                new Coordenada(3, 0)
        ), 5);
        this.santuario3 = new Santuario("Tailandia", 50,List.of(
                new Coordenada(20, 20),
                new Coordenada(20, 30),
                new Coordenada(30, 30),
                new Coordenada(30, 20),
                new Coordenada(20, 20)), 50);

        this.ubicacion2 = new Santuario("Quilmes", 50,List.of(
                new Coordenada(0, 3),
                new Coordenada(1, 3),
                new Coordenada(1, 4),
                new Coordenada(0, 4),
                new Coordenada(0, 3)
        ), 10);
        this.cementerio = new Cementerio("Tenebroso", 50,List.of(
                new Coordenada(3, 3),
                new Coordenada(4, 3),
                new Coordenada(4, 4),
                new Coordenada(3, 4),
                new Coordenada(3, 3)
        ), -10);
        this.cementerio2 = new Cementerio("oscuro", 90,List.of(
                new Coordenada(6, 3),
                new Coordenada(7, 3),
                new Coordenada(7, 4),
                new Coordenada(6, 4),
                new Coordenada(6, 3)
        ), -5);
        this.ubicacionNoConectada = new Santuario("MuyMuyLejano", 50,List.of(
                new Coordenada(9, 0),
                new Coordenada(10, 0),
                new Coordenada(10, 1),
                new Coordenada(9, 1),
                new Coordenada(9, 0)
        ), 30);


        ubicacionService.crear(santuario);
        ubicacionService.crear(santuario2);
        ubicacionService.crear(ubicacion2);
        ubicacionService.crear(santuario3);
        ubicacionService.crear(cementerio);
        ubicacionService.crear(cementerio2);
        ubicacionService.crear(ubicacionNoConectada);

        medium = new Medium("Max",100,50, new Coordenada(0.5, 0.5));
        medium2 = new Medium("Pepe",100,0, new Coordenada(0.5, 0.5));
        medium3 = new Medium("Sabrina",100,60, new Coordenada(3.5, 3.5));

        angelDebil = new Angel(10,"gabriel",new Coordenada(0.5, 0.5));
        angelDebil2 = new Angel(10,"fernando",new Coordenada(3.5, 0.5));
        angelFuerte = new Angel(60,"Rafael",new Coordenada(0.5, 0.5));
        angelEnCementerio = new Angel(50,"Di Maria",new Coordenada(3.5, 3.5));

        demonioDebil = new Demonio(5,"Rufi",new Coordenada(0.5, 0.5));
        demonioDebil2 = new Demonio(5,"An",new Coordenada(3.5, 0.5));
        demonioFuerte = new Demonio(50,"Adam",new Coordenada(0.5, 0.5));
        demonioEnCementerio =  new Demonio(5,"Lali",new Coordenada(3.5, 3.5));

        fakeRandomStrategy = new FakeRandomStrategyImpl();
        RandomGenerator.getInstance().setStrategy(fakeRandomStrategy);

    }
    @Test
    void seRecuperanTodosLosMediumsGuardados(){
        mediumService.crear(medium);
        mediumService.crear(medium2);

        var mediums = mediumService.recuperarTodos();

        assertEquals(2, mediums.size());
    }

    @Test
    void alRecuperarTodosLosMediumsEstandoLaBaseDeDatosVaciaRetornaListaVacia(){

        var mediums = mediumService.recuperarTodos();

        assertTrue(mediums.isEmpty());
    }

    @Test
    void seEliminaUnMediumDejaDeEstarPersistiendo(){
        mediumService.crear(medium);

        mediumService.eliminar(medium.getId());

        assertTrue(mediumService.recuperar(medium.getId()).isEmpty());
    }

    @Test
    public void noSePuedeEliminarUnMediumTodaviaReferenciado() {
        this.mediumService.crear(medium);
        this.espirituService.crear(angelDebil);
        this.espirituService.conectar(angelDebil.getId(), medium.getId());

        assertThrows(EntidadTodaviaReferenciadaException.class, () -> this.mediumService.eliminar(this.medium.getId()));
    }

    @Test
    void alEliminarUnMediumNoGuardadoNoHaceNada() {

        assertDoesNotThrow(() -> mediumService.eliminar(-1L));
    }

    @Test
    void alCrearYRecuperarMediumSeObtienenObjetosSimilares() {
        mediumService.crear(medium);

        Medium mediumRecuperado =  mediumService.recuperar(medium.getId()).get();

        assertEquals(medium.getId(), mediumRecuperado.getId());
        assertEquals(medium.getMana(),mediumRecuperado.getMana());
        assertEquals(medium.getManaMax(), mediumRecuperado.getManaMax());
        assertEquals(medium.getNombre(), mediumRecuperado.getNombre());
        assertEquals(this.medium.getCoordenada().getX(), mediumRecuperado.getCoordenada().getX());
        assertEquals(this.medium.getCoordenada().getY(), mediumRecuperado.getCoordenada().getY());
    }

    @Test
    void alCrearUnMediumSeLeAgregaUnId() {

        assertNull(medium.getId());

        mediumService.crear(medium);

        assertNotNull(medium.getId());
    }



    @Test
    void seActualizanUnMediumYAlRecuperarloLosCambiosSeVenReflejados() {
        mediumService.crear(medium);
        medium.setNombre("nombreModificado");

        mediumService.actualizar(medium);
        Medium recuperado = mediumService.recuperar(medium.getId()).get();

        assertEquals("nombreModificado", recuperado.getNombre());
    }

    @Test
    void seActualizaUnMediumInexistenteYNoSeLanzaNingunaExcepcion() {
        medium.setId(-1L);

        assertDoesNotThrow( () -> {
            mediumService.actualizar(medium);
        });
    }



    @Test
    void alRecuperarUnMediumQueNoEstaGuardadoSeRetornaEmpty() {
        Long idInexistente = 333L;

        assertTrue(mediumService.recuperar(idInexistente).isEmpty());

    }


    @Test
    void alConectarConUnEspirituElMediumAgregaEseEspirituALosSuyos() {
        espirituService.crear(angelDebil);
        mediumService.crear(medium);
        espirituService.conectar(angelDebil.getId(), medium.getId());
        List<Espiritu> espiritus = mediumService.espiritus(medium.getId());
        assertEquals(1,espiritus.size());

    }


    @Test
    void seRecuperanTodosLosEspiritusConLosQueEstaConectadoUnMedium() {
        mediumService.crear(medium);
        espirituService.crear(angelDebil);
        espirituService.crear(demonioDebil);

        espirituService.conectar(angelDebil.getId(), medium.getId());
        espirituService.conectar(demonioDebil.getId(), medium.getId());


        List<Espiritu> espirituRecuperados = mediumService.espiritus(medium.getId());

        assertEquals(2, espirituRecuperados.size());

    }

    @Test
    void seIntentaRecuperarLosEspiritusDeUnMediumQueNoExiseEntoncesSeDevuelveNull(){

        assertEquals(null,mediumService.espiritus(-1L));

    }

    @Test
    void seIntentaRecuperarTodosLosEspiritusDeUnMediumQueNoTieneEspiritusEntoncesSeDevuelveUnaListaVacia() {
        mediumService.crear(medium);

        List<Espiritu> espiritus = mediumService.espiritus(medium.getId());

        assertTrue(espiritus.isEmpty());
    }

    @Test
    void alExorcizarAUnMediumElAngelGanaYDisminuyeNivelDeConexionDelDemonio() {

        fakeRandomStrategy.addValorFijo(10);
        fakeRandomStrategy.addValorFijo(1);


        mediumService.crear(medium);
        mediumService.crear(medium2);

        espirituService.crear(angelFuerte);
        espirituService.crear(demonioFuerte);

        espirituService.conectar(angelFuerte.getId(),medium.getId());

        espirituService.conectar(demonioFuerte.getId(),medium2.getId());

        mediumService.exorcisar(medium.getId(),medium2.getId());

        Espiritu demonioRecuperado = espirituService.recuperar(demonioFuerte.getId()).get();
        Espiritu angelRecuperado = espirituService.recuperar(angelFuerte.getId()).get();

        assertEquals(15,demonioRecuperado.getNivelDeConexion());
        assertEquals(70,angelRecuperado.getNivelDeConexion());

    }

    @Test
    void alExorcizarAUnMediumElAngelPierdeYDisminuyeNivelDeConexionDelAngel(){

        fakeRandomStrategy.addValorFijo(1);
        fakeRandomStrategy.addValorFijo(100);


        mediumService.crear(medium);
        mediumService.crear(medium2);

        espirituService.crear(angelDebil);
        espirituService.crear(demonioFuerte);

        espirituService.conectar(angelDebil.getId(),medium.getId());

        espirituService.conectar(demonioFuerte.getId(),medium2.getId());

        mediumService.exorcisar(medium.getId(),medium2.getId());

        Espiritu demonioRecuperado = espirituService.recuperar(demonioFuerte.getId()).get();
        Espiritu angelRecuperado = espirituService.recuperar(angelDebil.getId()).get();

        assertEquals(50,demonioRecuperado.getNivelDeConexion());
        assertEquals(15,angelRecuperado.getNivelDeConexion());


    }

    @Test
    void alExorcizarAUnMediumElPrimerAngelVenceAlPrimerDemonioYElSegundoAngelPierdeContraElSegundoDemonio(){

        fakeRandomStrategy.addValorFijo(10);
        fakeRandomStrategy.addValorFijo(1);
        fakeRandomStrategy.addValorFijo(1);
        fakeRandomStrategy.addValorFijo(100);


        mediumService.crear(medium);
        mediumService.crear(medium2);

        espirituService.crear(angelFuerte);
        espirituService.crear(angelDebil);


        espirituService.crear(demonioDebil);
        espirituService.crear(demonioFuerte);


        espirituService.conectar(angelDebil.getId(),medium.getId());
        espirituService.conectar(angelFuerte.getId(),medium.getId());


        espirituService.conectar(demonioDebil.getId(),medium2.getId());
        espirituService.conectar(demonioFuerte.getId(),medium2.getId());

        mediumService.exorcisar(medium.getId(),medium2.getId());


        Espiritu angelFuerteRecuperado = espirituService.recuperar(angelFuerte.getId()).get();
        Espiritu angelDebilRecuperado = espirituService.recuperar(angelDebil.getId()).get();
        Espiritu demonioFuerteRecuperado = espirituService.recuperar(demonioFuerte.getId()).get();
        Espiritu demonioDebilRecuperado = espirituService.recuperar(demonioDebil.getId()).get();
        List<Espiritu> espiritusDeMedium2 = mediumService.espiritus(medium2.getId());

        assertEquals(0,demonioDebilRecuperado.getNivelDeConexion());
        assertEquals(50,demonioFuerteRecuperado.getNivelDeConexion());

        assertEquals(15,angelDebilRecuperado.getNivelDeConexion());
        assertEquals(70,angelFuerteRecuperado.getNivelDeConexion());

        assertEquals(1, espiritusDeMedium2.size());
        assertTrue(demonioDebilRecuperado.estaLibre());
    }

    @Test
    void alIntentarExorcizarSinAngelesLanzaUnError(){
        mediumService.crear(medium);
        mediumService.crear(medium2);

        assertThrows(ExorcistaSinAngelesException.class, () -> {
            mediumService.exorcisar(medium.getId(),medium2.getId());
        });
    }

    @Test
    void seIntentaExorcizarAUnMediumQueNoFuePersistidoYLanzaUnaExepcion(){

        mediumService.crear(medium);
        espirituService.crear(angelDebil);

        espirituService.conectar(angelDebil.getId(),medium.getId());

        Integer angelDebilNivelDeConex = angelDebil.getNivelDeConexion();

        assertThrows(EntidadInexistenteException.class, () -> {
            mediumService.exorcisar(medium.getId(),-1L);
        });

        assertEquals(angelDebilNivelDeConex, angelDebil.getNivelDeConexion());
    }

    @Test
    void unMediumNoPersistidoIntentaExorcizarAOtroEntoncesNoPasaNada(){

        mediumService.crear(medium2);
        espirituService.crear(demonioDebil);

        espirituService.conectar(demonioDebil.getId(),medium2.getId());

        Integer demonioDebilNivelDeConex = demonioDebil.getNivelDeConexion();

        assertDoesNotThrow( () -> {
            mediumService.exorcisar(-1L,medium2.getId());
        });

        assertEquals(demonioDebilNivelDeConex, demonioDebil.getNivelDeConexion());
    }

    @Test
    void unMediumPersistidoInvocaAUnEspirituQueNoEstaLibreYSuUbicacionCambia() {

        mediumService.crear(medium);
        espirituService.crear(angelDebil);
        int manaInicial = medium.getMana();

        //seteo una ubicacion al angel, que es diferente a la del medium
        angelDebil.setUbicacion(ubicacion2);
        espirituService.actualizar(angelDebil);

        mediumService.invocar(medium.getId(), angelDebil.getId());

        Medium mediumRecuperado = mediumService.recuperar(medium.getId()).get();
        Espiritu espirituRecuperado = espirituService.recuperar(angelDebil.getId()).get();

        Ubicacion ubicacionMedium =  ubicacionService.recuperar(mediumRecuperado.getUbicacion().getId()).get();
        Ubicacion ubicacionEspiritu = ubicacionService.recuperar(espirituRecuperado.getUbicacion().getId()).get();

        assertEquals(ubicacionMedium.getId(), ubicacionEspiritu.getId());
        assertEquals(espirituRecuperado.getCoordenada().getX(), medium.getCoordenada().getX());
        assertEquals(espirituRecuperado.getCoordenada().getY(), medium.getCoordenada().getY());

        assertEquals( manaInicial - 10 , mediumRecuperado.getMana());
    }

    @Test
    void unMediumNoPuedeInvocarAUnEspirituInexistente() {

        mediumService.crear(medium);

        assertThrows(EntidadInexistenteException.class, () -> mediumService.invocar(medium.getId(), -5L));
    }


    @Test
    void alRecibirMuchoDanhoElEspirituSeDesviculaDelMedium() {

        fakeRandomStrategy.addValorFijo(1);
        fakeRandomStrategy.addValorFijo(100);


        mediumService.crear(medium);
        mediumService.crear(medium2);

        espirituService.crear(angelDebil);
        espirituService.crear(demonioFuerte);

        espirituService.conectar(angelDebil.getId(),medium.getId());

        espirituService.conectar(demonioFuerte.getId(),medium2.getId());

        mediumService.exorcisar(medium.getId(),medium2.getId());

        assertTrue(medium.getAngeles().isEmpty());

    }




    @Test
    void CuandoUnMediumSeMueveAUnSantuarioSuAngelNORecibeElEfecto(){

        Ubicacion iglesia = new Cementerio("Iglesia de Santa Teresita",50,List.of(
                new Coordenada(12, 0),
                new Coordenada(13, 0),
                new Coordenada(13, 1),
                new Coordenada(12, 1),
                new Coordenada(12, 0)
        ), 10);
        ubicacionService.crear(iglesia);
        ubicacionService.conectar(cementerio.getId(),iglesia.getId());

        Angel libertador = new Angel(10,"Libertador",new Coordenada(3.5, 3.5));
        espirituService.crear(libertador);
        Medium medium = new Medium("Max",100,50, new Coordenada(3.5, 3.5));
        mediumService.crear(medium);
        espirituService.conectar(libertador.getId(),medium.getId());

        mediumService.mover(medium.getId(), 12.5, 0.5);

        Espiritu espirituRecuperado = espirituService.recuperar(libertador.getId()).get();

        assertEquals(espirituRecuperado.getNivelDeConexion(), 15);
        //10 de angel + 20% de Mana del medium - 5 de efecto de Cementerio,
    }

    @Test
    void unMediumSeMueveYSuAngelSeQuedaSinConexionEntoncesSeLibera(){

        Ubicacion wilde = new Cementerio("Wilde",50,List.of(
                new Coordenada(40, 40),
                new Coordenada(41, 40),
                new Coordenada(41, 41),
                new Coordenada(40, 41),
                new Coordenada(40, 40)
        ), 10);
        ubicacionService.crear(wilde);
        Ubicacion sarandi = new Cementerio("Sarandi",50,List.of(
                new Coordenada(5, 5),
                new Coordenada(6, 5),
                new Coordenada(6, 6),
                new Coordenada(5, 6),
                new Coordenada(5, 5)
        ), -5);
        ubicacionService.crear(sarandi);

        ubicacionService.conectar(cementerio.getId(), wilde.getId());
        ubicacionService.conectar(wilde.getId(), sarandi.getId());

        Angel mabel = new Angel(5,"Amon",new Coordenada(3.5, 3.5));
        espirituService.crear(mabel);

        Medium osvaldo = new Medium("Osvaldo",100,20, new Coordenada(3.5, 3.5));

        mediumService.crear(osvaldo);
        espirituService.conectar(mabel.getId(),osvaldo.getId());
        mediumService.mover(osvaldo.getId(), 40.5, 40.5); // dentro de  Wilde

        mediumService.mover(osvaldo.getId(), 5.5,5.5); // dentro de Sarandi

        Espiritu espirituRecuperado = espirituService.recuperar(mabel.getId()).get();

        assertEquals(espirituRecuperado.getNivelDeConexion(), 0);
        assertTrue(espirituRecuperado.estaLibre());
    }


    @Test
    void unMediumSeMueveYSuDemonioSeQuedaSinConexionEntoncesSeLibera(){

        Ubicacion iglesia = new Santuario("Iglesia de Santa Teresita",50,List.of(
                new Coordenada(40, 40),
                new Coordenada(41, 40),
                new Coordenada(41, 41),
                new Coordenada(40, 41),
                new Coordenada(40, 40)
        ), 10);
        ubicacionService.crear(iglesia);
        Ubicacion vaticano = new Santuario("vaticano",50,List.of(
                new Coordenada(0, 5),
                new Coordenada(1, 5),
                new Coordenada(1, 6),
                new Coordenada(0, 6),
                new Coordenada(0, 5)
        ), 40);
        ubicacionService.crear(vaticano);

        ubicacionService.conectar(santuario.getId(),iglesia.getId());
        ubicacionService.conectar(iglesia.getId(),vaticano.getId());
        ubicacionService.conectar(vaticano.getId(),iglesia.getId());

        Demonio amon = new Demonio(10,"Amon",new Coordenada(0.5, 0.5));
        espirituService.crear(amon);

        Medium osvaldo = new Medium("Osvaldo",100,50, new Coordenada(0.5, 0.5));

        mediumService.crear(osvaldo);
        espirituService.conectar(amon.getId(),osvaldo.getId());
        mediumService.mover(osvaldo.getId(), 40.5,40.5);

        mediumService.mover(osvaldo.getId(), 0.5,5.5); // dentro de vaticano;
        mediumService.mover(osvaldo.getId(), 40.5,40.5);

        Espiritu espirituRecuperado = espirituService.recuperar(amon.getId()).get();

        assertEquals(0, espirituRecuperado.getNivelDeConexion());
        assertTrue(espirituRecuperado.estaLibre());

    }

    @Test
    void CuandoUnMediumSeMueveAUnCementerioElAngelRecibeElEfectoDelCorrespondiente(){

        Ubicacion cementerio = new Cementerio("Recoleta",50,List.of(
                new Coordenada(12, 0),
                new Coordenada(13, 0),
                new Coordenada(13, 1),
                new Coordenada(12, 1),
                new Coordenada(12, 0)
        ), 25);
        ubicacionService.crear(cementerio);
        ubicacionService.conectar(cementerio.getId(), this.cementerio.getId());

        Angel libertador = new Angel(10,"Libertador",new Coordenada(12.5, 0.5));
        espirituService.crear(libertador);
        Medium medium = new Medium("Max",100,50, new Coordenada(12.5, 0.5));
        mediumService.crear(medium);
        espirituService.conectar(libertador.getId(),medium.getId());
        mediumService.mover(medium.getId(), 12.5,0.5);

        Espiritu espirituRecuperado = espirituService.recuperar(libertador.getId()).get();

        assertEquals(espirituRecuperado.getNivelDeConexion(), 15);
        //10 de angel + 20% de Mana del medium - 5 de efecto de Cementerio,
    }


    @Test
    void CuandoUnMediumSeMueveAUnSantuarioSuDemonioRecibeElEfectoCorrespondiente(){

        Ubicacion iglesia = new Santuario("Iglesia de Santa Teresita",50,List.of(
                new Coordenada(12, 0),
                new Coordenada(13, 0),
                new Coordenada(13, 1),
                new Coordenada(12, 1),
                new Coordenada(12, 0)
        ), 10);
        ubicacionService.crear(iglesia);
        ubicacionService.conectar(iglesia.getId(), santuario.getId());

        Demonio demonicaco = new Demonio(10,"demonicaco",new Coordenada(12.5, 0.5));
        espirituService.crear(demonicaco);
        Medium medium = new Medium("Max",100,50, new Coordenada(12.5, 0.5));
        mediumService.crear(medium);
        espirituService.conectar(demonicaco.getId(),medium.getId());
        mediumService.mover(medium.getId(), 0.5,0.5);

        Espiritu espirituRecuperado = espirituService.recuperar(demonicaco.getId()).get();

        assertEquals(espirituRecuperado.getNivelDeConexion(), 10);
        //10 de angel + 20% de Mana del medium - 10 de efecto de Santuario,
    }

    @Test
    void CuandoUnMediumSeMueveAUnCementerioSuDemonioNoRecibeElEfectoCorrespondiente(){

        Ubicacion sanMiguel = new Cementerio("San Miguel",50,List.of(
                new Coordenada(12, 0),
                new Coordenada(13, 0),
                new Coordenada(13, 1),
                new Coordenada(12, 1),
                new Coordenada(12, 0)
        ), 15);

        ubicacionService.crear(sanMiguel);
        ubicacionService.conectar(sanMiguel.getId(),cementerio.getId());

        Demonio demonicaco = new Demonio(10,"demonicaco",new Coordenada(12.5, 0.5));
        espirituService.crear(demonicaco);

        Medium medium = new Medium("Max",100,50, new Coordenada(12.5, 0.5));
        mediumService.crear(medium);
        espirituService.conectar(demonicaco.getId(),medium.getId());
        mediumService.mover(medium.getId(), 3.5,3.5);

        Espiritu espirituRecuperado = espirituService.recuperar(demonicaco.getId()).get();

        assertEquals(espirituRecuperado.getNivelDeConexion(), 20);
        //10 de angel + 20% de Mana del medium(10) - 0 de efecto de Cementerio,
    }

    @Test
    void seIntentaMoverUnMediumAUnaUbicacionLejanaYSeLanzaUnaExcepcion() {
        this.mediumService.crear(this.medium); //ubicacion santuario

        assertThrows(UbicacionLejanaException.class,
                ()-> this.mediumService.mover(this.medium.getId(), 9.5,9.5));

    }

    @Test
    public void unMediumNoPuedeInvocarAUnDemonioAUnSantuario() {
        Santuario santuario4 = new Santuario("Chocolandia", 50,List.of(
                new Coordenada(30.1, 30.1),
                new Coordenada(40, 30.1),
                new Coordenada(40, 40),
                new Coordenada(30.1, 40),
                new Coordenada(30.1, 30.1)), 20);

        medium.setCoordenada(new Coordenada(30.11, 30.11));
        demonioDebil2.setCoordenada(new Coordenada(29.99, 29.99));

        ubicacionService.crear(santuario4);
        mediumService.crear(medium); // está en un santuario
        espirituService.crear(demonioDebil2); // está en otro santuario

        mediumService.invocar(medium.getId(), demonioDebil2.getId());

        Medium mediumRecuperado = mediumService.recuperar(medium.getId()).get();
        Demonio demonioRecuperado = (Demonio) espirituService.recuperar(demonioDebil2.getId()).get();

        Long mediumUbicacionId = mediumRecuperado.getUbicacion().getId();
        Long demonioUbicacionId = demonioRecuperado.getUbicacion().getId();

        assertNotEquals(mediumUbicacionId, demonioUbicacionId); // siguen en diferente ubicación
        assertFalse(demonioRecuperado.getCoordenada().getX() == medium.getCoordenada().getX()
                && demonioRecuperado.getCoordenada().getY() == medium.getCoordenada().getY());
    }

    @Test
    public void unMediumNoPuedeInvocarAUnAngelAUnCementerio() {

        Cementerio cementerio2 = new Cementerio("Paseo del Brócoli", 50,List.of(
                new Coordenada(30.1, 30.1),
                new Coordenada(40, 30.1),
                new Coordenada(40, 40),
                new Coordenada(30.1, 40),
                new Coordenada(30.1, 30.1)), -15);

        medium3.setCoordenada(new Coordenada(30.11, 30.11));
        angelDebil.setCoordenada(new Coordenada(29.99, 29.99));

        ubicacionService.crear(cementerio2);
        mediumService.crear(medium3); // está en "cementerio"
        espirituService.crear(angelDebil); // está en "santuario"

        mediumService.invocar(medium3.getId(), angelDebil.getId());

        Medium mediumRecuperado = mediumService.recuperar(medium3.getId()).get();
        Angel angelRecuperado = (Angel) espirituService.recuperar(angelDebil.getId()).get();

        Long mediumUbicacionId = mediumRecuperado.getUbicacion().getId();
        Long angelUbicacionId = angelRecuperado.getUbicacion().getId();

        assertNotEquals(mediumUbicacionId, angelUbicacionId); // siguen en diferente ubicación
        assertFalse(angelRecuperado.getCoordenada().getX() == medium.getCoordenada().getX()
                && angelRecuperado.getCoordenada().getY() == medium.getCoordenada().getY());
    }

    @Test
    public void unMediumPuedeInvocarAUnAngelAUnSantuario() {

        Santuario santuario4 = new Santuario("Chocolandia", 50,List.of(
                new Coordenada(30.1, 30.1),
                new Coordenada(40, 30.1),
                new Coordenada(40, 40),
                new Coordenada(30.1, 40),
                new Coordenada(30.1, 30.1)), 15);

        medium.setCoordenada(new Coordenada(30.11, 30.11));
        angelDebil2.setCoordenada(new Coordenada(29.99, 29.99));

        ubicacionService.crear(santuario4);
        mediumService.crear(medium); // está en un santuario
        espirituService.crear(angelDebil2); // está en otro santuario

        mediumService.invocar(medium.getId(), angelDebil2.getId());

        Medium mediumRecuperado = mediumService.recuperar(medium.getId()).get();
        Angel angelRecuperado = (Angel) espirituService.recuperar(angelDebil2.getId()).get();

        Long mediumUbicacionId = mediumRecuperado.getUbicacion().getId();
        Long angelUbicacionId = angelRecuperado.getUbicacion().getId();

        assertEquals(mediumUbicacionId, angelUbicacionId); // misma ubicación
        assertEquals(angelRecuperado.getCoordenada().getX(), medium.getCoordenada().getX());
        assertEquals(angelRecuperado.getCoordenada().getY(), medium.getCoordenada().getY());
    }

    @Test
    public void unMediumPuedeInvocarUnDemonioAUnCementerio() {
        Cementerio cementerio4 = new Cementerio("Paseo del Brócoli", 50,List.of(
                new Coordenada(30.1, 30.1),
                new Coordenada(40, 30.1),
                new Coordenada(40, 40),
                new Coordenada(30.1, 40),
                new Coordenada(30.1, 30.1)), -15);

        demonioDebil2.setCoordenada(new Coordenada(30.11, 30.11));
        medium3.setCoordenada(new Coordenada(30.2, 30.2));

        ubicacionService.crear(cementerio4);

        mediumService.crear(medium3); // está en un santuario
        espirituService.crear(demonioDebil2); // está en un cementerio

        mediumService.invocar(medium3.getId(), demonioDebil2.getId());

        Medium mediumRecuperado = mediumService.recuperar(medium3.getId()).get();
        Demonio demonioRecuperado = (Demonio) espirituService.recuperar(demonioDebil2.getId()).get();

        Long mediumUbicacionId = mediumRecuperado.getUbicacion().getId();
        Long demonioUbicacionId = demonioRecuperado.getUbicacion().getId();

        assertEquals(mediumUbicacionId, demonioUbicacionId); // misma ubicacion
        assertEquals(demonioRecuperado.getCoordenada().getX(), mediumRecuperado.getCoordenada().getX());
        assertEquals(demonioRecuperado.getCoordenada().getY(), mediumRecuperado.getCoordenada().getY());
    }

    @Test
    void seHaceDescansarAUnMediumEnUnSantuarioYSuDemonioNoRecuperaConexion(){

        mediumService.crear(medium);
        espirituService.crear(demonioDebil);

        int nivelDeConexionInicial = demonioDebil.getNivelDeConexion();

        espirituService.conectar(demonioDebil.getId(), medium.getId()); // aumenta en 10 el nivel de conexión, por conectar con el demonio
        mediumService.descansar(medium.getId());

        Demonio demonio = (Demonio) espirituService.recuperar(demonioDebil.getId()).get();

        assertEquals(nivelDeConexionInicial + 10, demonio.getNivelDeConexion());
    }

    @Test
    void seHaceDescansarAUnMediumEnUnCementerioYSuAngelNoRecuperaConexion(){

        mediumService.crear(medium3);
        espirituService.crear(angelEnCementerio);

        int nivelDeConexionInicial = angelEnCementerio.getNivelDeConexion();

        espirituService.conectar(angelEnCementerio.getId(), medium3.getId()); // aumenta en 12 el nivel de conexión, por conectar con el angel
        mediumService.descansar(medium3.getId());

        Angel angel = (Angel) espirituService.recuperar(angelEnCementerio.getId()).get();

        assertEquals(nivelDeConexionInicial + 12, angel.getNivelDeConexion());
    }

    @Test
    void seHaceDescansarAUnMediumEnUnSantuarioYSuAngelRecuperaConexionEnLaMismaCantidadQueLaEnergiaDelSantuario(){

        mediumService.crear(medium);
        espirituService.crear(angelDebil); // empieza con 10 de nivel de conexión
        // el santuario en el que se encuentran tiene 50 de energía

        espirituService.conectar(angelDebil.getId(), medium.getId()); // gana 10 de nivel de conexión -> 20

        mediumService.descansar(medium.getId()); // gana 50 de nivel de conexión (por el santuario) -> 70

        Espiritu recuperado1 = espirituService.recuperar(angelDebil.getId()).get();

        assertEquals(70, recuperado1.getNivelDeConexion());
    }

    @Test
    void seHaceDescansarAUnMediumEnUnCementerioYSuDemonioRecuperaConexionEnLaMismaCantidadQueLaEnergiaDelSantuario(){

        mediumService.crear(medium3);
        espirituService.crear(demonioEnCementerio); // empieza con 5 de nivel de conexión
        // el cementerio en el que se encuentran tiene 50 de energía

        espirituService.conectar(demonioEnCementerio.getId(), medium3.getId()); // gana 12 de nivel de conexión -> 17

        mediumService.descansar(medium3.getId()); // gana 50 de nivel de conexión (por el santuario) -> 67

        Espiritu recuperado1 = espirituService.recuperar(demonioEnCementerio.getId()).get();

        assertEquals(67, recuperado1.getNivelDeConexion());
    }

    @Test
    void seHaceDescansarAUnMediumEnUnSantuarioRecuperandoManaEn150PorCientoDeLaEnergiaDelSantuario(){
        medium.setMana(10);
        mediumService.crear(medium); // tiene 10 de mana, 100 de mana max, el santuario tiene 50 de energía
        espirituService.crear(angelDebil);

        espirituService.conectar(angelDebil.getId(), medium.getId());

        mediumService.descansar(medium.getId()); // gana 75 de mana, queda en 85

        Medium recuperado = mediumService.recuperar(medium.getId()).get();

        assertEquals(85, recuperado.getMana());
    }

    @Test
    void seHaceDescansarAUnMediumEnUnCementerioRecuperandoManaEn50PorCientoDeLaEnergiaDelCementerio(){
        medium3.setMana(10);
        mediumService.crear(medium3); // tiene 10 de mana, 100 de mana max, el cementerio tiene 50 de energía
        espirituService.crear(angelDebil);


        mediumService.descansar(medium3.getId()); // gana 25 de mana, queda en 35

        Medium recuperado = mediumService.recuperar(medium3.getId()).get();

        assertEquals(35, recuperado.getMana());
    }

    @Test
    void seIntentaHacerDescansarAUnMediumQueNoFuePersistidoYNoHaceNada(){
        assertDoesNotThrow( () -> {
            mediumService.descansar(-1L);
        });

    }

    @Test
    public void unMediumSeMueveAUnPuntoLejanoDaElErrorCorrespondiente() {
        this.mediumService.crear(this.medium);

        assertThrows(UbicacionLejanaException.class, () -> this.mediumService.mover(this.medium.getId(), 100.0, -90.0));
    }

    @Test
    public void unMediumIntentaExorcisarAOtroMediumQueEstaEnOtraUbicacionEntoncesSeLanzaUnaExcepcion() {
        this.mediumService.crear(this.medium);
        this.espirituService.crear(angelDebil);
        this.espirituService.conectar(angelDebil.getId(), medium.getId());
        this.mediumService.crear(this.medium3);

        assertThrows(MediumEnDistintaUbicacionException.class, () -> this.mediumService.exorcisar(this.medium.getId(), this.medium3.getId()));
    }

    @Test
    void unMediumNoPuedeInvocarAUnEspirituAMasDe50KmDeDistancia() {

        demonioDebil.setCoordenada(new Coordenada(21, 21));

        mediumService.crear(medium);
        espirituService.crear(demonioDebil);

        assertThrows(EspirituMuyLejanoException.class, () -> mediumService.invocar(medium.getId(), demonioDebil.getId()));
    }

    @Test
    void unMediumPuedeInvocarAUnEspirituAAprox50KmDeDistancia() {

        angelDebil.setCoordenada(new Coordenada(21, 21));
        medium.setCoordenada(new Coordenada(20.71, 20.649)); // distancia de 50 km

        mediumService.crear(medium);
        espirituService.crear(angelDebil);

        Espiritu espirituInvocado = mediumService.invocar(medium.getId(), angelDebil.getId());
        assertEquals(espirituInvocado.getCoordenada().getX(), medium.getCoordenada().getX());
        assertEquals(espirituInvocado.getCoordenada().getY(), medium.getCoordenada().getY());

    }

    @Test
    public void siUnaUbicacionEstaMuyLejosDaElErrorCorrespondiente() {
        mediumService.crear(medium);

        Ubicacion ubicacionLejana = new Santuario("Laprida", 50, List.of(
                new Coordenada(-90, -90),
                new Coordenada(-80, -90),
                new Coordenada(-80, -80),
                new Coordenada(-90, -80),
                new Coordenada(-90, -90)), -20);

        ubicacionService.crear(ubicacionLejana);
        ubicacionService.conectar(medium.getUbicacion().getId(), ubicacionLejana.getId()); // la Ubicacion NoConectada esta muylejos

        List<Point> coordenadas = ubicacionLejana.getCoordenadas();
        Point unPuntoEnUbicacion = coordenadas.getFirst();

        assertThrows(UbicacionLejanaException.class, () -> mediumService.mover(medium.getId(), unPuntoEnUbicacion.getX(), unPuntoEnUbicacion.getY()));

    }


    @AfterEach
    void eliminarModelo() {
        testService.eliminarEspiritus();
        testService.eliminarMediums();
        testService.eliminarUbicaciones();
        fakeRandomStrategy.cleanUp();
    }


}
