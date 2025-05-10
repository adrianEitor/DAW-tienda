import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AccionEliminarDelCarrito implements Accion {

    @Override
    public String ejecutar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            Carrito carrito = (Carrito) session.getAttribute("carrito");
            if (carrito != null) {
                String indexStr = request.getParameter("index");
                if (indexStr != null) {
                    try {
                        int index = Integer.parseInt(indexStr);
                        carrito.eliminarItem(index);
                        // session.setAttribute("carrito", carrito); // Opcional, el objeto en sesión se modifica
                    } catch (NumberFormatException e) {
                        System.err.println("AccionEliminarDelCarrito: Índice inválido: " + indexStr);
                        request.setAttribute("errorCarrito", "No se pudo eliminar el ítem, índice inválido.");
                    }
                }
            }
        }
        // Siempre redirigir a verCarrito para mostrar el estado actualizado
        response.sendRedirect(request.getContextPath() + "/app?accion=verCarrito");
        return null; // Indica que la respuesta ya fue manejada
    }
}