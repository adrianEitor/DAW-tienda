import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.SQLException;

public class PagarServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            HttpSession session = request.getSession(false);

            if (session != null) {
                    // Limpiar el carrito después de procesar el pago
                    session.removeAttribute("carrito");
                    
                    // Meter el pedido en la base de datos
                    BaseDatos bd = (BaseDatos) session.getAttribute("bd");
                    String emailUsuario = (String) session.getAttribute("usuario");
                    Pedido pedido = new Pedido(bd.obtenerUsuarioPorEmail(emailUsuario).getId(), Float.parseFloat(request.getParameter("importeFinal")));
                    bd.agregarPedido(pedido);
                }
            } catch (SQLException e) {
                // Manejo de la excepción
                e.printStackTrace();  // Para depuración
                request.setAttribute("error", "Error de base de datos. Intenta nuevamente.");
                request.getRequestDispatcher("pago.jsp").forward(request, response);
            }
            response.sendRedirect("/index.jsp");
        }
}