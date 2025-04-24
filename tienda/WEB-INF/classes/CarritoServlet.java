import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class CarritoServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Obtener o crear la sesión
        HttpSession session = request.getSession(true);
        
        // Obtener o crear el carrito en la sesión
        List<CD> carrito = (List<CD>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<CD>();
            session.setAttribute("carrito", carrito);
        }
        
        // Procesar el formulario
        String cdSeleccionado = request.getParameter("cd");
        String cantidadStr = request.getParameter("cantidad");
        int cantidad = 1;
        
        try {
            cantidad = Integer.parseInt(cantidadStr);
        } catch (NumberFormatException e) {
            cantidad = 1;
        }
        
        // Parsear el CD seleccionado usando StringTokenizer
        if (cdSeleccionado != null && !cdSeleccionado.isEmpty()) {
            StringTokenizer tokenizer = new StringTokenizer(cdSeleccionado, "|");
            String titulo = tokenizer.nextToken().trim();
            String artista = tokenizer.nextToken().trim();
            String pais = tokenizer.nextToken().trim();
            String precioStr = tokenizer.nextToken().trim().replace("$", "").trim();
            
            try {
                float precio = Float.parseFloat(precioStr);
                // Agregar al carrito
                CD cd = new CD(titulo, artista, pais, precio, cantidad);
                carrito.add(cd);
            } catch (NumberFormatException e) {
                // Manejar error en el formato del precio
                System.err.println("Error al parsear el precio: " + precioStr);
            }
        }
        
        // Redirigir a outro servlet:
        response.sendRedirect();
    }
}