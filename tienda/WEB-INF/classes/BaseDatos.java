import java.sql.*;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.NamingException;

public class BaseDatos {
    private DataSource ds;
    private UsuariosDAO usuariosDAO;
    private PedidosDAO pedidosDAO;

    public BaseDatos() throws SQLException {
        try {
            Context ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/tienda");
        } catch (NamingException e) {
            throw new RuntimeException("Error al obtener DataSource", e);
        }
        // Inicializar los DAOs
        this.usuariosDAO = new UsuariosDAO();
        this.pedidosDAO = new PedidosDAO();
    }

    // Métodos fachada de Usuarios
    public void agregarUsuario(Usuario usuario) throws SQLException {
        try (Connection conexion = ds.getConnection()){
            usuariosDAO.agregarUsuario(usuario, conexion);
        }
    }

    public Usuario obtenerUsuarioPorEmail(String email) throws SQLException {
        try (Connection conexion = ds.getConnection()){
            return usuariosDAO.obtenerUsuarioPorEmail(email, conexion);
        }
    }

    public List<Usuario> listarUsuarios() throws SQLException {
        try (Connection conexion = ds.getConnection()){
            return usuariosDAO.listarUsuarios(conexion);
        }
    }

    // Métodos fachada de Pedidos
    public void agregarPedido(Pedido pedido) throws SQLException {
        try (Connection conexion = ds.getConnection()){
            pedidosDAO.agregarPedido(pedido, conexion);
        }
    }

    public List<Pedido> obtenerPedidosPorUsuario(int usuarioId) throws SQLException {
        try (Connection conexion = ds.getConnection()){
            return pedidosDAO.obtenerPedidosPorUsuario(usuarioId, conexion);
        }        
    }
}
