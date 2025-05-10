import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AccionProcesarPago implements Accion {

    @Override
    public String ejecutar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        float importeConfirmado = 0f;

        if (session == null) {
            request.setAttribute("error", "Su sesión ha expirado. Por favor, inicie sesión nuevamente.");
            return "/login.jsp"; // O /WEB-INF/jsp/login.jsp
        }

        Usuario usuarioAutenticado = (Usuario) session.getAttribute("usuarioAutenticado");
        Carrito carrito = (Carrito) session.getAttribute("carrito");
        BaseDatos bd = (BaseDatos) session.getAttribute("bd");

        if (usuarioAutenticado == null) {
            request.setAttribute("error", "Debe iniciar sesión para completar el pago.");
            return "/login.jsp";
        }
        if (carrito == null || carrito.isVacio()) {
            request.setAttribute("mensaje", "Su carrito está vacío. No se puede procesar el pago.");
            return "/WEB-INF/jsp/verCarrito.jsp"; // O a index
        }
        if (bd == null) {
            System.err.println("AccionProcesarPago: BaseDatos no encontrada en sesión.");
            request.setAttribute("error", "Error interno del sistema (BD no disponible).");
            request.setAttribute("importeFinal", carrito.getTotalGeneral());
            return "/WEB-INF/jsp/pago.jsp";
        }

        String importeFinalStr = request.getParameter("importeFinalConfirmado");
        try {
            if (importeFinalStr != null) {
                importeConfirmado = Float.parseFloat(importeFinalStr);
                if (Math.abs(importeConfirmado - carrito.getTotalGeneral()) > 0.01) {
                    throw new NumberFormatException("Discrepancia de importe.");
                }
            } else {
                 throw new NumberFormatException("Importe final no proporcionado.");
            }

            Pedido nuevoPedido = new Pedido(usuarioAutenticado.getId(), importeConfirmado);
            nuevoPedido.setFechaPedido(new Timestamp(System.currentTimeMillis()));
            
            bd.agregarPedido(nuevoPedido);
            
            carrito.vaciarCarrito();
            session.setAttribute("carrito", carrito); // Guardar carrito vacío

            request.setAttribute("mensajeExito", "¡Gracias! Su pedido ha sido procesado.");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error con el importe del pago: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error de base de datos al guardar el pedido: " + e.getMessage());
        }
        
        request.setAttribute("importeFinal", importeConfirmado); // Siempre pasar el importe
        return "/pago.jsp"; // Siempre ir a pago.jsp para mostrar resultado
    }
}