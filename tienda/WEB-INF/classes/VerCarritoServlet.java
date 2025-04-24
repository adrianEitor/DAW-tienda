// VerCarritoServlet.java
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class VerCarritoServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("carrito") == null) {
            response.sendRedirect("index.html");
            return;
        }
        
        List<CD> carrito = (List<CD>) session.getAttribute("carrito");
        
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Carrito de Compras</title>");
        out.println("</head>");
        out.println("<body bgcolor=\"#FDF5E6\">");
        out.println("<h1 align=\"center\">Carrito de Compras</h1>");
        
        out.println("<table border=\"1\" align=\"center\">");
        out.println("<tr>");
        out.println("<th>TITULO DEL CD</th>");
        out.println("<th>Cantidad</th>");
        out.println("<th>Importe</th>");
        out.println("<th>Acción</th>");
        out.println("</tr>");
        
        float total = 0;
        for (int i = 0; i < carrito.size(); i++) {
            CD cd = carrito.get(i);
            out.println("<tr>");
            out.println("<td>" + cd.getTitulo() + " | " + cd.getArtista() + " | " + cd.getPais() + " | $" + cd.getPrecio() + "</td>");
            out.println("<td>" + cd.getCantidad() + "</td>");
            out.println("<td>" + cd.getImporte() + "</td>");
            out.println("<td><a href=\"EliminarCDServlet?index=" + i + "\">Eliminar</a></td>");
            out.println("</tr>");
            total += cd.getImporte();
        }
        
        out.println("<tr>");
        out.println("<td colspan=\"2\"><b>IMPORTE TOTAL</b></td>");
        out.println("<td><b>" + total + "</b></td>");
        out.println("<td></td>");
        out.println("</tr>");
        out.println("</table>");
        
        out.println("<p align=\"center\">");
        out.println("<a href=\"index.html\">Sigo comprando</a> | ");
        out.println("<a href=\"PagarServlet\">Me largo a pagar</a>");
        out.println("</p>");
        
        out.println("</body>");
        out.println("</html>");
    }
}