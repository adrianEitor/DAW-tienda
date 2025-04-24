// PagarServlet.java
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class PagarServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession(false);
        float total = 0;
        
        if (session != null) {
            List<CD> carrito = (List<CD>) session.getAttribute("carrito");
            if (carrito != null) {
                for (CD cd : carrito) {
                    total += cd.getImporte();
                }
                // Clear the cart
                session.removeAttribute("carrito");
            }
        }
        
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Pago</title>");
        out.println("</head>");
        out.println("<body bgcolor=\"#FDF5E6\">");
        out.println("<h1 align=\"center\">Gracias por su compra</h1>");
        out.println("<p align=\"center\">Importe total: $" + total + "</p>");
        out.println("<p align=\"center\"><a href=\"index.html\">Volver a la tienda</a></p>");
        out.println("</body>");
        out.println("</html>");
    }
}