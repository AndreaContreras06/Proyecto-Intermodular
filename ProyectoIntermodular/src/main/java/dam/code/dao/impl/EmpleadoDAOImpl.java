package dam.code.dao.impl;

import dam.code.config.DatabaseConfig;
import dam.code.dao.EmpleadoDAO;
import dam.code.exceptions.EmpleadoException;
import dam.code.exceptions.UsuarioException;
import dam.code.models.Empleado;
import dam.code.models.Usuario;
import dam.code.models.utils.Plan;
import dam.code.models.utils.Rol;
import dam.code.utils.CryptPassword;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class EmpleadoDAOImpl implements EmpleadoDAO {

    @Override
    public void registrar(Empleado empleado, String password) throws EmpleadoException {
        String sql = "INSERT INTO empleados (dni, nombre, apellido, telefono, email, password, rol) VALUES (?, ?, ?, ?, ?, ?, ?)";

        String passwordHash = CryptPassword.hashPassword(password);

        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, empleado.getDni());
            ps.setString(2, empleado.getNombre());
            ps.setString(3, empleado.getApellido());
            ps.setString(4, empleado.getTelefono());
            ps.setString(5, empleado.getEmail());
            ps.setString(6, passwordHash);
            ps.setString(7, empleado.getRol().name());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new EmpleadoException(e.getMessage());
        }
    }

    @Override
    public Empleado login(String dni, String password) throws EmpleadoException {
        String sql = "SELECT * FROM empleados WHERE dni = ?";

        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dni);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hashBD = rs.getString("password");

                if (!CryptPassword.checkPassword(password, hashBD)) {
                    throw new EmpleadoException("Usuario o contraseña incorrectos");
                }

                return new Empleado(
                        rs.getInt("id"),
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        Rol.valueOf(rs.getString("rol"))
                );
            }

            throw new EmpleadoException("Usuario o contraseña incorrectos");

        } catch (SQLException e) {
            throw new EmpleadoException(e.getMessage());
        }
    }
}

