import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * ============================================================================
 * CLASE AccionCalcularPago
 * ============================================================================
 * IMPLEMENTA: Interfaz Accion
 *
 * PROPÓSITO:
 * Esta clase se encarga de la lógica para preparar la información necesaria
 * antes de que el usuario proceda a la página de confirmación de pago.
 * Su responsabilidad principal es:
 * 
 * 1. Obtener el carrito de la compra actual del usuario desde la sesión HTTP.
 * 
 * 2. Calcular el importe total a pagar basándose en los ítems del carrito.
 * 
 * 3. Poner este importe total y el propio objeto Carrito como atributos en el
 *    objeto request para que la página JSP de pago (pago.jsp) pueda acceder a ellos.
 * 
 * 4. Devolver la ruta al pago.jsp para que el AppController realice un forward.
 *
 * Es invocada por el AppController cuando el parámetro "accion" es "calcularPago"
 * (típicamente cuando el usuario hace clic en "Me largo a pagar" desde la vista del carrito).
 */
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

            // Si se encontró un objeto Carrito y este no está vacío...
            if (carrito != null && !carrito.isVacio()) 
            {
                // ...calcular el importe total llamando al método del JavaBean 'Carrito'
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

            // En este escenario, el usuario probablemente no está logueado o su sesión ha expirado.
            // Podría ser apropiado redirigir al login en lugar de continuar a pago.jsp.
            // Ejemplo:
            // request.setAttribute("error", "Su sesión ha expirado o no ha iniciado sesión para pagar.");
            // return "/login.jsp"; 
            // O, si se permite ver la página de pago con 0.0:
            // importeFinalAPagar permanece 0.0f.
        }

         // --- 2. PREPARACIÓN DE DATOS PARA LA VISTA (JSP) ---

        // Poner el importe total calculado como un atributo en el objeto request.
        // El JSP pago.jsp podrá acceder a este valor usando EL: ${importeFinal}.
        request.setAttribute("importeFinal", importeFinalAPagar);

        // Poner también el objeto Carrito completo en el request (incluso si está vacío).
        // Esto permite que pago.jsp pueda, si es necesario, mostrar un resumen del carrito
        // antes de la confirmación final, o manejar la visualización de un carrito vacío.
        // El JSP lo accederá como ${carritoParaConfirmar}.
        request.setAttribute("carritoParaConfirmar", carrito); // El JSP pago.jsp podría usarlo
        
        // --- 3. DEVOLVER RUTA DE LA VISTA ---

        // Devolver la ruta al archivo JSP que mostrará la información del pago.
        // El AppController usará esta cadena para realizar el RequestDispatcher.forward().
        return "/pago.jsp"; // Ruta al JSP de confirmación/pago
    }
}