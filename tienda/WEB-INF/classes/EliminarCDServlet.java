// EliminarCDServlet.java
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class EliminarCDServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session != null) {

            Carrito carrito = (Carrito) session.getAttribute("carrito");

            if (carrito != null) {
                try {
                    int index = Integer.parseInt(request.getParameter("index"));
                    
                    carrito.eliminarItem(index);

                    session.setAttribute("carrito", carrito);

                } catch (NumberFormatException e) {
                    // Ignore invalid index
                }
            }
        }
        
        response.sendRedirect(request.getContextPath() + "/VerCarritoServlet");
    }
}