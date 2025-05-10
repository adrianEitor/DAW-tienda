import java.io.IOException;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AccionAgregarAlCarrito implements Accion {

    @Override
    public String ejecutar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(true);
        Carrito carrito = (Carrito) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new Carrito();
            session.setAttribute("carrito", carrito);
        }
        
        String cdSeleccionado = request.getParameter("cd");
        String cantidadStr = request.getParameter("cantidad");
        int cantidad = 1;
        
        try {
            if (cantidadStr != null && !cantidadStr.trim().isEmpty()) {
                cantidad = Integer.parseInt(cantidadStr.trim());
                if (cantidad <= 0) cantidad = 1;
            }
        } catch (NumberFormatException e) {
            cantidad = 1;
        }
        
        if (cdSeleccionado != null && !cdSeleccionado.isEmpty()) {
            StringTokenizer tokenizer = new StringTokenizer(cdSeleccionado, "|");
            if (tokenizer.countTokens() >= 4) {
                String titulo = tokenizer.nextToken().trim();
                String artista = tokenizer.nextToken().trim();
                String pais = tokenizer.nextToken().trim();
                String precioStr = tokenizer.nextToken().trim().replace("$", "").trim();
                try {
                    float precio = Float.parseFloat(precioStr);
                    CD nuevoCd = new CD();
                    nuevoCd.setTitulo(titulo);
                    nuevoCd.setArtista(artista);
                    nuevoCd.setPais(pais);
                    nuevoCd.setPrecio(precio);
                    nuevoCd.setCantidad(cantidad);
                    carrito.agregarItem(nuevoCd);
                } catch (NumberFormatException e) {
                    System.err.println("AccionAgregarAlCarrito: Error al parsear precio: " + precioStr);
                    request.setAttribute("errorCarrito", "Error al procesar el CD seleccionado.");
                }
            } else {
                System.err.println("AccionAgregarAlCarrito: Formato de CD seleccionado incorrecto.");
                request.setAttribute("errorCarrito", "Datos del CD incompletos.");
            }
        }
        
        // Después de agregar, redirigimos a VerCarrito para mostrar el estado actualizado
        // y evitar problemas de reenvío de formulario si el usuario recarga.
        // Por lo tanto, esta acción no devuelve una vista JSP directamente.
        response.sendRedirect(request.getContextPath() + "/app?accion=verCarrito"); // Llama al AppController con la acción verCarrito
        return null; // Indica que la respuesta ya fue manejada (sendRedirect)
    }
}