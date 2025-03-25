package ar.edu.unq.epersgeist.persistencia.dao.jdbc;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public record JDBCEspirituDAO() implements EspirituDAO {



    public Espiritu crear(Espiritu espiritu) {

        String sql = "INSERT INTO espiritu (tipo, nombre, nivelDeConexion) VALUES (?,?,?)";
        JDBCConnector.getInstance().execute(conn  -> {
            try {
                var ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, espiritu.getTipo());
                ps.setString(2, espiritu.getNombre());
                ps.setInt(3, espiritu.getNivelDeConexion());
                ps.execute();
                ps.getGeneratedKeys().next();
                long serialId = ps.getGeneratedKeys().getLong(1);

                espiritu.setId(serialId);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return null;
        });

        return espiritu;
    }

    public Espiritu recuperar(Long idDelEspiritu) {
        return JDBCConnector.getInstance().execute( conn -> {
            try {
                var ps = conn.prepareStatement("SELECT nombre, tipo, nivelDeConexion FROM espiritu WHERE id = ?");
                ps.setLong(1, idDelEspiritu);
                var resultSet = ps.executeQuery();
                resultSet.next();

                Espiritu espiritu = new Espiritu(
                        resultSet.getString("tipo"),
                        resultSet.getInt("nivelDeConexion"),
                        resultSet.getString("nombre")
                );

                espiritu.setId(idDelEspiritu);

                return espiritu;


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public List<Espiritu> recuperarTodos() {
        return JDBCConnector.getInstance().execute( conn -> {
            try {
                var ps = conn.prepareStatement("SELECT * FROM espiritu ORDER BY nombre ASC");
                var resultSet = ps.executeQuery();
                List<Espiritu> espiritus = new ArrayList<Espiritu>();

                while (resultSet.next()) {
                    Espiritu espiritu = new Espiritu(
                            resultSet.getString("tipo"),
                            resultSet.getInt("nivelDeConexion"),
                            resultSet.getString("nombre")
                    );
                    espiritu.setId(resultSet.getLong("id"));
                    espiritus.add(espiritu);
                }

                return espiritus;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void actualizar(Espiritu espiritu) {
        JDBCConnector.getInstance().execute( conn -> {
            try {
                var ps = conn.prepareStatement("UPDATE espiritu SET tipo = ?, nivelDeConexion = ?, nombre = ? WHERE id = ?");
                ps.setString(1, espiritu.getTipo());
                ps.setInt(2, espiritu.getNivelDeConexion());
                ps.setString(3, espiritu.getNombre());
                ps.setLong(4, espiritu.getId());

                ps.execute();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }

    public void eliminar(Long idDelEspiritu) {
        JDBCConnector.getInstance().execute (conn -> {
            try {
                var ps = conn.prepareStatement("DELETE FROM espiritu WHERE id = ? ");
                ps.setLong(1, idDelEspiritu);
                return ps.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public JDBCEspirituDAO() {
        try {
            var uri = getClass().getClassLoader().getResource("createAll.sql").toURI();
            var initializeScript = Files.readString(Paths.get(uri));
            JDBCConnector.getInstance().execute(conn -> {
                try {
                    var ps = conn.prepareStatement(initializeScript);
                    return ps.execute();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}