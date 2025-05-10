import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AccionCalcularPago implements Accion {

    @Override
    public String ejecutar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        float importeFinalAPagar = 0.0f;
        Carrito carrito = null;

        if (session != null) {
            carrito = (Carrito) session.getAttribute("carrito");
            if (carrito != null && !carrito.isVacio()) {
                importeFinalAPagar = carrito.getTotalGeneral();
            } else {
                if (carrito == null) carrito = new Carrito(); // Para pasar un objeto no nulo al JSP
            }
        } else {
            // No hay sesión, no se puede calcular.
            carrito = new Carrito(); // Para pasar un objeto no nulo al JSP
             // Opcionalmente redirigir al login si se requiere sesión para esta acción
            // response.sendRedirect(request.getContextPath() + "/app?accion=mostrarLogin&error=sesionExpirada");
            // return null;
        }

        request.setAttribute("importeFinal", importeFinalAPagar);
        request.setAttribute("carritoParaConfirmar", carrito); // El JSP pago.jsp podría usarlo
        
        return "/pago.jsp"; // Ruta al JSP de confirmación/pago
    }
}