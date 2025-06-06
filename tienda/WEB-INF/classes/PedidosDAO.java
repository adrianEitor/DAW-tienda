import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidosDAO {

    // Método para agregar un pedido
    public void agregarPedido(Pedido pedido, Connection conexion) throws SQLException {
        String sql = "INSERT INTO pedidos (usuario_id, importe_total) VALUES (?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) { // Usamos la conexión pasada en el constructor
            stmt.setInt(1, pedido.getUsuarioId());
            stmt.setFloat(2, pedido.getImporteTotal());
            stmt.executeUpdate();
        }
    }

    // Método para obtener los pedidos de un usuario por su ID
    public List<Pedido> obtenerPedidosPorUsuario(int usuarioId, Connection conexion) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE usuario_id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) { // Usamos la conexión pasada en el constructor
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                pedidos.add(new Pedido(
                    rs.getInt("id"),
                    rs.getInt("usuario_id"),
                    rs.getFloat("importe_total"),
                    rs.getTimestamp("fecha_pedido")
                ));
            }
        }
        return pedidos;
    }
}
