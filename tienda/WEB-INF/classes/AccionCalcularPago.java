import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AccionCalcularPago implements Accion 
{

    /**
     * ------------------------------------------------------------------------
     * MÉTODO ejecutar (Implementación de la interfaz Accion)
     * ------------------------------------------------------------------------
     * Realiza el cálculo del importe total y prepara los datos para la vista de pago.
     */
    @Override
    public String ejecutar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        System.out.println("AccionCalcularPago: Iniciando cálculo del pago."); // Log para depuración
        
        // --- 1. OBTENCIÓN DE LA SESIÓN Y EL CARRITO ---

        // Intenta obtener la sesión HTTP existente. Si no existe, request.getSession(false) devuelve null.
        // No se crea una nueva sesión aquí si no existe, ya que se asume que si el usuario
        // va a pagar, ya debería tener una sesión con un carrito.
        HttpSession session = request.getSession(false);

        float importeFinalAPagar = 0.0f; // Inicializa el importe total a 0.
        Carrito carrito = null; // Inicializa la referencia al carrito a null.

        // Solo proceder si existe una sesión.
        if (session != null) 
        {
             System.out.println("AccionCalcularPago: Sesión encontrada.");

            // Intentar obtener el objeto Carrito de la sesión.
            carrito = (Carrito) session.getAttribute("carrito");

            // Si se encontró un objeto Carrito y este no está vacío
            if (carrito != null && !carrito.isVacio()) 
            {
                // Calcular el importe total llamando al método del JavaBean 'Carrito'
                importeFinalAPagar = carrito.getTotalGeneral();
            } 
            else 
            {
                // Si el carrito de la sesión es null o está vacío.
                if (carrito == null) carrito = new Carrito(); // Crear un Carrito vacío para pasar al JSP y evitar NullPointerExceptions.
            }
        } 
        else 
        {
            // No hay sesión, no se puede calcular.
            System.out.println("AccionCalcularPago: No hay sesión activa. Creando un Carrito vacío para la vista.");

            carrito = new Carrito(); // Para pasar un objeto no nulo al JSP
        }

         // --- 2. PREPARACIÓN DE DATOS PARA LA VISTA (JSP) ---

        // Poner el importe total calculado como un atributo en el objeto request.
        request.setAttribute("importeFinal", importeFinalAPagar);

        // Poner también el objeto Carrito completo en el request (incluso si está vacío).
        // Esto permite que pago.jsp pueda, si es necesario, mostrar un resumen del carrito
        // antes de la confirmación final, o manejar la visualización de un carrito vacío.
        request.setAttribute("carritoParaConfirmar", carrito); // El JSP pago.jsp podría usarlo
        
        // --- 3. DEVOLVER RUTA DE LA VISTA ---
        // El AppController usará esta cadena para realizar el RequestDispatcher.forward().
        return "/pago.jsp"; // Ruta al JSP de confirmación/pago
    }
}