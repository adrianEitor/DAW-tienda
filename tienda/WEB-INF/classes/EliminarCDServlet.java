// EliminarCDServlet.java
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class EliminarCDServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            List<CD> carrito = (List<CD>) session.getAttribute("carrito");
            if (carrito != null) {
                try {
                    int index = Integer.parseInt(request.getParameter("index"));
                    if (index >= 0 && index < carrito.size()) {
                        CD cd = carrito.get(index);
                        cd.setCantidad(cd.getCantidad()-1);
                        if(cd.getCantidad() == 0){
                            carrito.remove(index);
                        }
                    }
                } catch (NumberFormatException e) {
                    // Ignore invalid index
                }
            }
        }
        response.sendRedirect("VerCarritoServlet");
    }
}