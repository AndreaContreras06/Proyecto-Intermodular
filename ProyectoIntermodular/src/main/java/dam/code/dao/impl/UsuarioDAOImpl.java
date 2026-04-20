package dam.code.dao.impl;

import dam.code.config.DatabaseConfig;
import dam.code.dao.UsuarioDAO;
import dam.code.exceptions.UsuarioException;
import dam.code.models.Usuario;
import dam.code.models.utils.Plan;
import dam.code.utils.CryptPassword;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public void registrar(Usuario usuario, String password) throws UsuarioException {
        String sql = "INSERT INTO usuarios (dni, nombre, apellido, telefono, email, password, plan) VALUES (?, ?, ?, ?, ?, ?, ?)";

        String passwordHash = CryptPassword.hashPassword(password);

        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario.getDni());
            ps.setString(2, usuario.getNombre());
            ps.setString(3, usuario.getApellido());
            ps.setString(4, usuario.getTelefono());
            ps.setString(5, usuario.getEmail());
            ps.setString(6, passwordHash);
            ps.setString(7, usuario.getPlan().name());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new UsuarioException(e.getMessage());
        }
    }

    @Override
    public Usuario login(String dni, String password) throws UsuarioException {
        String sql = "SELECT * FROM usuarios WHERE dni = ?";

        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dni);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hashBD = rs.getString("password");

                if (!CryptPassword.checkPassword(password, hashBD)) {
                    throw new UsuarioException("Usuario o contraseña incorrectos");
                }

                return new Usuario(
                        rs.getInt("id"),
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        Plan.valueOf(rs.getString("plan"))
                );
            }

            throw new UsuarioException("Usuario o contraseña incorrectos");

        } catch (SQLException e) {
            throw new UsuarioException(e.getMessage());
        }
    }
}
