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
        
        Carrito carrito = null;

        if (session != null) {
        
            carrito = (Carrito) session.getAttribute("carrito");
        }

        if (carrito == null) {
            carrito = new Carrito();
        }

        // Poner los datos en el request para que el JSP pueda acceder a ellos
        request.setAttribute("carrito", carrito); // Puede ser null o vacío

        // El cálculo del total general ya no se hace aquí,
        // se accede a través de ${carrito.totalGeneral} en el JSP.
        
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