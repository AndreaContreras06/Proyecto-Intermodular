package dam.code.dao.impl;

import dam.code.config.DatabaseConfig;
import dam.code.dao.ActividadDAO;
import dam.code.dao.UsuarioDAO;
import dam.code.exceptions.ActividadException;
import dam.code.models.Actividad;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActividadDAOImpl implements ActividadDAO {

    @Override
    public void registrar(Actividad actividad) throws ActividadException {
        String sql = "INSERT INTO actividades (actividad, duracion) VALUES (?, ?)";

        try (Connection conexion = DatabaseConfig.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, actividad.getActividad());
            ps.setInt(3, actividad.getDuracion());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new ActividadException(e.getMessage());
        }
    }

    @Override
    public List<Actividad> listar() throws ActividadException {
        List<Actividad> actividades = new ArrayList<>();
        String sql = "SELECT * FROM actividades";

        try (Connection con = DatabaseConfig.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                actividades.add (new Actividad(
                        rs.getInt("id"),
                        rs.getString("actividad"),
                        rs.getInt("duracion")
                ));
            }

        } catch (SQLException e) {
            throw new ActividadException(e.getMessage());
        }

        return actividades;
    }

    @Override
    public List<Actividad> obtenerActividadPorUsuario(int idUsuario) throws ActividadException {
        List<Actividad> actividades = new ArrayList<>();
        String sql = """
                SELECT a.id, a.actividad, a.duracion,
                    COUNT(i.id_actividad) AS interes
                FROM actividades a
                INNER JOIN interes i ON a.id = i.id_actividad
                WHERE i.id_usuario = ?
                GROUP BY a.id, a.actividad, a.duracion
                """;

        try (Connection conexion = DatabaseConfig.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Actividad actividad = new Actividad(
                        rs.getInt("id"),
                        rs.getString("actividad"),
                        rs.getInt("duracion")
                );
                actividad.setInteres (rs.getInt("interes"));
                actividades.add(actividad);
            }

        } catch (SQLException e) {
            throw new ActividadException(e.getMessage());
        }

        return actividades;
    }

    @Override
    public void interes(int idUsuario, int idActividad) throws ActividadException {
        String sql = "INSERT INTO interes (id_usuario, id_actividad) VALUES (?, ?)";

        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, idActividad);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new ActividadException(e.getMessage());
        }
    }
}
