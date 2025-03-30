package ar.edu.unq.epergeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.servicios.impl.EspirituServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class EspirituServiceImplTest {
    public static final Medium MEDIUM = new Medium("Brujita", null, null);
    private final EspirituServiceImpl espirituService = new EspirituServiceImpl();
    private Espiritu espiritu;

//    Descomentar para ver logs.

//    @BeforeAll
//    static void setupLogging() {
//        try {
//            // Cargamos la configuración de logging
//            getLogManager().readConfiguration(
//                JDBCPersonajeDAOTest.class.getClassLoader().getResourceAsStream("logging.properties")
//            );
//        } catch (Exception e) {
//            System.err.println("No se pudo cargar la configuración de logging: " + e.getMessage());
//        }
//    }

    @BeforeEach
    void crearEspiritu() {
        espiritu = new Angel(
                "Pepe",
                10
        );
    }

//    @Test
//    void alEliminarUnEspirituDejaDeEstarPersistido() {
//        espirituService.crear(espiritu);
//
////        assertThrows(Exception.class, () -> {
//        espirituService.recuperar(espiritu.getId());
////        });
//    }
//
//    @Test
//    void alGuardarYLuegoRecuperarSeObtieneObjetosSimilares() {
//
//        espirituService.crear(espiritu);
//
//        var otroEspiritu = espirituService.recuperar(espiritu.getId());
//
//        assertEquals(espiritu.getTipo(), otroEspiritu.getTipo());
//        assertEquals(espiritu.getNombre(), otroEspiritu.getNombre());
//        assertEquals(espiritu.getNivelDeConexion(), otroEspiritu.getNivelDeConexion());
////         no puedo comparar ID porque otroEspiritu no tiene ID setteado todavía
//
//
//        Assertions.assertNotEquals(espiritu, otroEspiritu);
//
//    }
//
//    @Test
//    void alGuardarActualizarYLuegoRecuperarSeObtieneObjetosSimilares() {
//
//        espirituService.crear(espiritu);
//        espirituService.actualizar(espiritu);
//
//        var otroEspiritu = espirituService.recuperar(espiritu.getId());
//
//        assertEquals(espiritu.getTipo(), otroEspiritu.getTipo());
//        assertEquals(espiritu.getNombre(), otroEspiritu.getNombre());
//        assertEquals(espiritu.getNivelDeConexion(), otroEspiritu.getNivelDeConexion());
////         no puedo comparar ID porque otroPepe no tiene ID setteado todavía
//
//
//        Assertions.assertNotEquals(espiritu, otroEspiritu);
//
//    }
//
//
//    @Test
//    void alConectarUnEspirituSuNivelDeConexionAumentaYSePersiste() {
//        espirituService.crear(espiritu);
//
//        espirituService.conectar(espiritu.getId(), MEDIUM);
//
//        Espiritu espirituRecuperado = espirituService.recuperar(espiritu.getId());
//
//        assertEquals(10, espirituRecuperado.getNivelDeConexion());
//
//    }
//
//
//
//
//
//    @Test
//    void alRecuperarUnEspirituQueNoExisteLanzaError() {
//
//        assertThrows(Exception.class, () -> {
//            espirituService.recuperar(-1L);
//        });
//    }
//
//    @Test
//    void alRecuperarTodosEspíritusPeroNoHayNingunoDevuelveListaVacía() {
//
//        assertTrue(espirituService.recuperarTodos().isEmpty());
//    }
//
//
//    @Test
//    void recuperarATodosLosEspiritusVaEnOrdenAscendente() {
//
//        Espiritu gonzalo = new Espiritu(
//                "Fantasma",
//                0,
//                "Gonzalo"
//        );
//
//        Espiritu jaimito = new Espiritu(
//                "Fantasma",
//                0,
//                "Jaimito"
//        );
//
//        espirituService.crear(espiritu);
//        espirituService.crear(gonzalo);
//        espirituService.crear(jaimito);
//
//        ArrayList<Long> idDeTodos = new ArrayList<Long>();
//
//        espirituService.recuperarTodos().forEach( (espiritu) -> {
//            idDeTodos.add(espiritu.getId());
//        });
//
//        assertEquals(Arrays.asList(gonzalo.getId(), jaimito.getId(), espiritu.getId()), idDeTodos);
//
//
//
//    }
//
//
//
//
//
//    @AfterEach
//    void emilinarModelo() {
//        JDBCConnector.getInstance().execute(conn -> {
//            try {
//                var ps = conn.prepareStatement("DELETE from espiritu");
//                ps.execute();
//
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//
//            return null;
//        });
//    }
}