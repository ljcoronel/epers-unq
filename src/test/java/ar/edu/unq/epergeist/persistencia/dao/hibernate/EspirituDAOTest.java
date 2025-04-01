/*package ar.edu.unq.epergeist.persistencia.dao.hibernate;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.jdbc.JDBCConnector;
import ar.edu.unq.epersgeist.persistencia.dao.jdbc.JDBCEspirituDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class EspirituDAOTest {
    public static final Medium MEDIUM = new Medium("Brujita", null, null);
    private JDBCEspirituDAO espirituDao = new JDBCEspirituDAO();
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
        espiritu = new Espiritu(
                "Fantasma",
                0,
                "Pepe"
        );
    }

    @Test
    void alEliminarUnEspirituDejaDeEstarPersistido() {
        espirituDao.crear(espiritu);

//        assertThrows(Exception.class, () -> {
            espirituDao.recuperar(espiritu.getId());
//        });
    }

    @Test
    void alGuardarYLuegoRecuperarSeObtieneObjetosSimilares() {

        espirituDao.crear(espiritu);

        var otroPepe = espirituDao.recuperar(espiritu.getId());

        assertEquals(espiritu.getTipo(), otroPepe.getTipo());
        assertEquals(espiritu.getNombre(), otroPepe.getNombre());
        assertEquals(espiritu.getNivelDeConexion(), otroPepe.getNivelDeConexion());
//         no puedo comparar ID porque otroPepe no tiene ID setteado todavía


        Assertions.assertNotEquals(espiritu, otroPepe);

    }

    @Test
    void alGuardarActualizarYLuegoRecuperarSeObtieneObjetosSimilares() {

        espirituDao.crear(espiritu);
        espirituDao.actualizar(espiritu);

        var otroEspiritu = espirituDao.recuperar(espiritu.getId());

        assertEquals(espiritu.getTipo(), otroEspiritu.getTipo());
        assertEquals(espiritu.getNombre(), otroEspiritu.getNombre());
        assertEquals(espiritu.getNivelDeConexion(), otroEspiritu.getNivelDeConexion());
//         no puedo comparar ID porque otroEspiritu no tiene ID setteado todavía


        Assertions.assertNotEquals(espiritu, otroEspiritu);

    }

    @Test
    void alRecuperarAUnEspirituConectadoSuNivelDeConexionPersisteSiSeLoActualiza() {

        espirituDao.crear(espiritu);
        espiritu.aumentarConexion(MEDIUM);
        espirituDao.actualizar(espiritu);

        var otroEspiritu = espirituDao.recuperar(espiritu.getId());

        assertEquals(10, espiritu.getNivelDeConexion());
        Assertions.assertNotEquals(espiritu, otroEspiritu);

    }

    @Test
    void alConectarUnEspirituSuNivelDeConexionAumentaEn10() {

        espiritu.aumentarConexion(MEDIUM);

        assertEquals(10, espiritu.getNivelDeConexion());

    }


    @Test
    void alRecuperarUnEspirituQueNoExisteLanzaError() {

        assertThrows(Exception.class, () -> {
            espirituDao.recuperar(-1L);
        });
    }

    @Test
    void alRecuperarTodosEspíritusPeroNoHayNingunoDevuelveListaVacía() {

        assertTrue(espirituDao.recuperarTodos().isEmpty());
    }


    @Test
    void recuperarATodosLosEspiritusVaEnOrdenAscendente() {

        Espiritu gonzalo = new Espiritu(
                "Fantasma",
                0,
                "Gonzalo"
        );

        Espiritu jaimito = new Espiritu(
                "Fantasma",
                0,
                "Jaimito"
        );

        espirituDao.crear(espiritu);
        espirituDao.crear(gonzalo);
        espirituDao.crear(jaimito);

        ArrayList<Long> idDeTodos = new ArrayList<Long>();

        espirituDao.recuperarTodos().forEach( (espiritu) -> {
            idDeTodos.add(espiritu.getId());
        });

        assertEquals(Arrays.asList(gonzalo.getId(), jaimito.getId(), espiritu.getId()), idDeTodos);

        espirituDao.eliminar(espiritu.getId());
        espirituDao.eliminar(gonzalo.getId());
        espirituDao.eliminar(jaimito.getId());


    }





    @AfterEach
    void emilinarModelo() {
        JDBCConnector.getInstance().execute(conn -> {
            try {
                var ps = conn.prepareStatement("DELETE from espiritu");
                ps.execute();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return null;
        });
    }
}*/