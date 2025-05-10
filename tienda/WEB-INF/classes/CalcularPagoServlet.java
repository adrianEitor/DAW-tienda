import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CalcularPagoServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        float totalAPagar = 0.0f;

        Carrito carrito = null;

        if (session != null) {
            
            carrito = (Carrito) session.getAttribute("carrito");

            if (carrito != null && !carrito.isVacio())
            {   
                totalAPagar = carrito.getTotalGeneral();
            } 
        }

        request.setAttribute("importeFinal", totalAPagar);

        // Reenviar al JSP de pago
        RequestDispatcher dispatcher = request.getRequestDispatcher("/pago.jsp");
        dispatcher.forward(request, response);
    }
}