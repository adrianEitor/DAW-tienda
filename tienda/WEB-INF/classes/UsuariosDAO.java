import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuariosDAO {

    // Método para agregar un usuario
    public void agregarUsuario(Usuario usuario, Connection conexion) throws SQLException {
        String sql = "INSERT INTO usuarios (email, password, nombre, numero_tarjeta) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) { // Usamos la conexión pasada en el constructor
            stmt.setString(1, usuario.getEmail());
            stmt.setString(2, usuario.getPassword());
            stmt.setString(3, usuario.getNombre());
            stmt.setString(4, usuario.getNumeroTarjeta());
            stmt.executeUpdate();
        }
    }

    // Método para obtener un usuario por email
    public Usuario obtenerUsuarioPorEmail(String email, Connection conexion) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) { // Usamos la conexión pasada en el constructor
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Usuario(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("nombre"),
                    rs.getString("numero_tarjeta")
                );
            }
            return null;
        }
    }

    // Método para listar todos los usuarios
    public List<Usuario> listarUsuarios(Connection conexion) throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) { // Usamos la conexión pasada en el constructor
            while (rs.next()) {
                usuarios.add(new Usuario(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("nombre"),
                    rs.getString("numero_tarjeta")
                ));
            }
        }
        return usuarios;
    }
}
