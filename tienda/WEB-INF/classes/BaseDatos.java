import java.sql.*;
import java.util.List;

public class BaseDatos {
    private static final String URL = "jdbc:postgresql://localhost:5432/tienda";
    private static final String USUARIO = "postgres";
    private static final String PASSWORD = "usuario";

    private Connection conexion;
    private UsuariosDAO usuariosDAO;
    private PedidosDAO pedidosDAO;

    public BaseDatos() throws SQLException {
        this.conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        this.usuariosDAO = new UsuariosDAO(conexion);
        this.pedidosDAO = new PedidosDAO(conexion);
    }

    // Métodos fachada de Usuarios
    public void agregarUsuario(Usuario usuario) throws SQLException {
        usuariosDAO.agregarUsuario(usuario);
    }

    public Usuario obtenerUsuarioPorEmail(String email) throws SQLException {
        return usuariosDAO.obtenerUsuarioPorEmail(email);
    }

    public List<Usuario> listarUsuarios() throws SQLException {
        return usuariosDAO.listarUsuarios();
    }

    // Métodos fachada de Pedidos
    public void agregarPedido(Pedido pedido) throws SQLException {
        pedidosDAO.agregarPedido(pedido);
    }

    public List<Pedido> obtenerPedidosPorUsuario(int usuarioId) throws SQLException {
        return pedidosDAO.obtenerPedidosPorUsuario(usuarioId);
    }

    public void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
