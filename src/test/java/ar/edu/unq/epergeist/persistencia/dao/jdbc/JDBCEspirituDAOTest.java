package ar.edu.unq.epergeist.persistencia.dao.jdbc;

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

public class JDBCEspirituDAOTest {
    private JDBCEspirituDAO espirituDao = new JDBCEspirituDAO();
    private Espiritu pepe;

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
        pepe = new Espiritu(
                "Fantasma",
                0,
                "Pepe"
        );
    }

    @Test
    void alEliminarUnEspirituDejaDeEstarPersistido() {
        espirituDao.crear(pepe);

//        assertThrows(Exception.class, () -> {
            espirituDao.recuperar(pepe.getId());
//        });
    }

    @Test
    void alGuardarYLuegoRecuperarSeObtieneObjetosSimilares() {

        espirituDao.crear(pepe);

        var otroPepe = espirituDao.recuperar(pepe.getId());

        assertEquals(pepe.getTipo(), otroPepe.getTipo());
        assertEquals(pepe.getNombre(), otroPepe.getNombre());
        assertEquals(pepe.getNivelDeConexion(), otroPepe.getNivelDeConexion());
//         no puedo comparar ID porque otroPepe no tiene ID setteado todavía


        Assertions.assertNotEquals(pepe, otroPepe);

    }

    @Test
    void alGuardarActualizarYLuegoRecuperarSeObtieneObjetosSimilares() {

        espirituDao.crear(pepe);
        espirituDao.actualizar(pepe);

        var otroPepe = espirituDao.recuperar(pepe.getId());

        assertEquals(pepe.getTipo(), otroPepe.getTipo());
        assertEquals(pepe.getNombre(), otroPepe.getNombre());
        assertEquals(pepe.getNivelDeConexion(), otroPepe.getNivelDeConexion());
//         no puedo comparar ID porque otroPepe no tiene ID setteado todavía


        Assertions.assertNotEquals(pepe, otroPepe);

    }

    @Test
    void alRecuperarAUnEspirituConectadoSuNivelDeConexionPersisteSiSeLoActualiza() {

        espirituDao.crear(pepe);
        pepe.aumentarConexion(new Medium("Brujita", null, null));
        espirituDao.actualizar(pepe);

        var otroPepe = espirituDao.recuperar(pepe.getId());

        assertEquals(10, pepe.getNivelDeConexion());
        Assertions.assertNotEquals(pepe, otroPepe);

    }

    @Test
    void alConectarUnEspirituSuNivelDeConexionAumentaEn10() {

        pepe.aumentarConexion(new Medium("Brujita", null, null));

        assertEquals(10, pepe.getNivelDeConexion());

    }

    @Test
    void alConectarUnEspirituSuNivelDeConexionNoAumentaMasDe100() {

        pepe = new Espiritu(
                "Fantasma",
                99,
                "Pepe"
        );

        pepe.aumentarConexion(new Medium("Brujita", null, null));


        assertEquals(100, pepe.getNivelDeConexion());
        //no puede ser negativo porque no hay métodos que lo seteen negativo por ahora
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

        espirituDao.crear(pepe);
        espirituDao.crear(gonzalo);
        espirituDao.crear(jaimito);

        ArrayList<Long> idDeTodos = new ArrayList<Long>();

        espirituDao.recuperarTodos().forEach( (espiritu) -> {
            idDeTodos.add(espiritu.getId());
        });

        assertEquals(Arrays.asList(gonzalo.getId(), jaimito.getId(), pepe.getId()), idDeTodos);

        espirituDao.eliminar(pepe.getId());
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
}