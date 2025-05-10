import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AccionVerCarrito implements Accion {

    @Override
    public String ejecutar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Carrito carrito = null;

        if (session != null) {
            carrito = (Carrito) session.getAttribute("carrito");
        }

        if (carrito == null) {
            carrito = new Carrito();
            // Opcional: si quieres que un carrito vacío persista en sesión:
            // if (session != null) session.setAttribute("carrito", carrito);
        }
        
        request.setAttribute("carrito", carrito);
        
        // Devuelve la ruta al JSP para que el Front Controller haga el forward
        return "/verCarrito.jsp"; // Asumiendo que tus JSPs están en WEB-INF/jsp
    }
}