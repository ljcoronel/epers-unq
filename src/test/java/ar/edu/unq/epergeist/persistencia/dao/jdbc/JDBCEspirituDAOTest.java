package ar.edu.unq.epergeist.persistencia.dao.jdbc;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.persistencia.dao.jdbc.JDBCEspirituDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static java.util.logging.LogManager.getLogManager;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void alGuardarYLuegoRecuperarSeObtieneObjetosSimilares() {

        espirituDao.crear(pepe);
        System.out.print(pepe.getId());

//        Los personajes son iguales

        var otroPepe = espirituDao.recuperar(pepe.getId());

        assertEquals(pepe.getTipo(), otroPepe.getTipo());
        assertEquals(pepe.getNombre(), otroPepe.getNombre());
        assertEquals(pepe.getNivelDeConexion(), otroPepe.getNivelDeConexion());
//         no puedo comparar ID porque otroPepe no tiene ID setteado todavía


        Assertions.assertNotEquals(pepe, otroPepe);

        espirituDao.eliminar(pepe.getId());
    }

    @Test
    void alRecuperarUnEspirituQueNoExisteLanzaError() {

        assertThrows(Exception.class, () -> {
            espirituDao.recuperar(-1L);
        });
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

//    @Test
//    void alEliminarUnEspirituQueNoExisteNoHaceNada() {
//
//        assertThrows(Exception.class, () -> {
//            espirituDao.eliminar(-1L);
//        });
//    }



    @AfterEach
    void emilinarModelo() {
//        espirituDao.eliminar(pepe.getId());
//        no siempre se usa
    }
}