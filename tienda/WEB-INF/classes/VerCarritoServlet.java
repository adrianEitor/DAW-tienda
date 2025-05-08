import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Si estás usando anotaciones para el mapeo (Servlet 3.0+):
// import javax.servlet.annotation.WebServlet;
// @WebServlet("/VerCarritoServlet")
public class VerCarritoServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false); // No crear nueva sesión si no existe
        List<CD> carrito = null;
        float totalGeneral = 0.0f;

        if (session != null) {
            // El warning de unchecked se puede suprimir con @SuppressWarnings("unchecked")
            // si estás seguro del tipo.
            @SuppressWarnings("unchecked")
            List<CD> carritoEnSesion = (List<CD>) session.getAttribute("carrito");
            carrito = carritoEnSesion; // Asignar incluso si es null
        }

        if (carrito != null && !carrito.isEmpty()) {
            for (CD cd : carrito) {
                totalGeneral += cd.getImporte();
            }
        }

        // Poner los datos en el request para que el JSP pueda acceder a ellos
        request.setAttribute("carrito", carrito); // Puede ser null o vacío
        request.setAttribute("totalGeneral", totalGeneral);

        // Reenviar la petición al JSP para que muestre los datos
        RequestDispatcher dispatcher = request.getRequestDispatcher("/verCarrito.jsp");
        dispatcher.forward(request, response);
    }

    // doPost podría simplemente llamar a doGet si la lógica es la misma
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}