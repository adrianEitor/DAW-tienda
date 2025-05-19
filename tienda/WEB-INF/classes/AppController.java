import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ============================================================================
 * CLASE AppController - Servlet Front Controller
 * ============================================================================
 * EXTENDS: HttpServlet
 *
 * PROPÓSITO:
 * Esta clase actúa como un Front Controller. Su principal responsabilidad es ser el
 * punto de entrada único para la mayoría de las peticiones de la aplicación
 * que involucran lógica de negocio o navegación compleja.
 *
 * FUNCINAMIENTO:
 *     a. El clientE envía una petición a una URL mapeada a este AppController
 *        (ej., '/app?accion=verCarrito').
 * 
 *     b. AppController (a través de doGet/doPost que llaman a processRequest)
 *        extrae el parámetro "accion".
 * 
 *     c. Basado en el valor de "accion", instancia la clase Accion concreta apropiada.
 * 
 *     d. Invoca el método ejecutar() de la instancia de Accion.
 * 
 *     e. La clase Accion realiza el trabajo (interactúa con el Modelo, prepara datos
 *        para la Vista) y devuelve una cadena que es la ruta al JSP, o null si
 *        la Accion ya manejó la respuesta (ej., con sendRedirect).
 * 
 *     f. Si se devolvió una ruta de JSP, AppController usa NavegadorVistas.irAPagina()
 *        (que hace RequestDispatcher.forward()) para pasar el control al JSP.
 *
 */
public class AppController extends HttpServlet 
{
     /**
     * ------------------------------------------------------------------------
     * MÉTODO processRequest
     * ------------------------------------------------------------------------
     * Maneja la lógica principal para todas las peticiones GET y POST recibidas
     * por este Front Controller.
     * Extrae el parámetro accion, selecciona la clase Accion correspondiente,
     * ejecuta la acción, y finalmente despacha a la vista apropiada.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        // Establecer la codificación de caracteres para la petición.
        request.setCharacterEncoding("UTF-8");
        
         // --- 1. DETERMINAR LA ACCIÓN SOLICITADA ---

        // Obtener el parámetro accion de la URL (ej., /app?accion=verCarrito)
        // o de un campo oculto en un formulario.
        String accionParam = request.getParameter("accion");

        // Si no se proporciona el parámetro accion, o está vacío, se establece una acción por defecto.
        if (accionParam == null || accionParam.trim().isEmpty()) 
        {
            accionParam = "verIndex"; 
        }

        System.out.println("AppController: Acción recibida -> " + accionParam); // Log para depuración

        Accion accionEjecutar = null; // Variable para mantener la instancia de la acción a ejecutsr
        String vistaDestino = null; // Variable para la ruta del JSP al que se hará forward

        // --- 2. SELECCIÓN DE LA CLASE Accion (WORKER/HELPER) ---

        // Se utiliza un bloque switch para determinar qué clase Accion instanciar
        // basándose en el valor de accionParam.
        switch (accionParam.toLowerCase()) 
        {
            case "agregarcd": // Si la acción es "agregarcd" (enviada desde el formulario de index.jsp)
                accionEjecutar = new AccionAgregarAlCarrito();
                break;
            case "vercarrito":
                accionEjecutar = new AccionVerCarrito();
                break;
            case "eliminarcd":
                accionEjecutar = new AccionEliminarDelCarrito();
                break;
            case "calcularpago": // Desde el enlace "Me largo a pagar" en verCarrito.jsp
                accionEjecutar = new AccionCalcularPago();
                break;
            case "procesarpago": // Desde el formulario de confirmación en pago.jsp
                accionEjecutar = new AccionProcesarPago();
                break;
            case "login": // Desde el formulario de login.jsp
                accionEjecutar = new AccionLogin();
                break;
            case "registro": // Desde el formulario de registro (está en login.jsp)
                accionEjecutar = new AccionRegistro();
                break;
            case "verindex": // Acción por defecto para mostrar la página de inicio.
                vistaDestino = "/index.jsp"; 
                break;
            default: // Si el valor de 'accion' no coincide con ninguno de los casos anteriores.
                System.err.println("AppController: Acción desconocida - " + accionParam);
                request.setAttribute("error", "La acción solicitada ('" + accionParam + "') no es válida o no está implementada.");
                vistaDestino = "/error.jsp"; // Un JSP de error genérico
        }

        // --- 3. EJECUCIÓN DE LA ACCIÓN Y DESPACHO A LA VISTA ---
        try
        {
            // Si se encontró e instanció una clase Accion para el accionParam
            if (accionEjecutar != null) 
            {
                System.out.println("AppController: Ejecutando acción: " + accionEjecutar.getClass().getSimpleName());

                // Se llama a su método ejecutar().
                // Este método realizará la lógica de negocio y devolverá:
                //   La ruta (String) al JSP al que se debe hacer forward.
                //   o
                //   null si la acción ya manejó la respuesta (ej., con response.sendRedirect()).
                vistaDestino = accionEjecutar.ejecutar(request, response);
            }

            // Si vistaDestino no es null (lo que significa que la acción no hizo un sendRedirect
            // y quiere que se haga un forward a un JSP) Y si la respuesta no ha sido "committed"
            // (es decir, si sendRedirect o escritura directa al response no ha ocurrido aún)
            if (vistaDestino != null && !response.isCommitted()) 
            {
                System.out.println("AppController: Realizando forward a la vista: " + vistaDestino);

                // Se utiliza la clase de utilidad 'NavegadorVistas' para realizar el forward.
                NavegadorVistas.irAPagina(vistaDestino, request, response);
            }
            else if (vistaDestino == null && !response.isCommitted())
            {
                 System.err.println("AppController: La acción devolvió null para vistaDestino, pero la respuesta no fue committed. Revise la lógica de la acción: " + (accionEjecutar != null ? accionEjecutar.getClass().getSimpleName() : "Acción directa a vista nula"));
            } 
            else 
            {
                System.out.println("AppController: La respuesta ya fue committed por la acción (ej. sendRedirect) o la acción no requiere forward.");
            }

        } 
        // Captura genérica para errores inesperados en las Acciones
        catch (Exception e) 
        { 
            System.err.println("AppController: EXCEPCIÓN CAPTURADA en processRequest: " + e.getMessage());
            e.printStackTrace(); // Loguear el error completo
            request.setAttribute("error", "Ocurrió un error inesperado en la aplicación: " + e.getMessage());
        }
    }

    /**
     * ------------------------------------------------------------------------
     * MÉTODO doGet
     * ------------------------------------------------------------------------
     * Maneja las peticiones HTTP GET. Simplemente delega el procesamiento
     * al método processRequest.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * ------------------------------------------------------------------------
     * MÉTODO doPost
     * ------------------------------------------------------------------------
     * Maneja las peticiones HTTP POST. Simplemente delega el procesamiento
     * al método processRequest.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * ------------------------------------------------------------------------
     * MÉTODO getServletInfo
     * ------------------------------------------------------------------------
     * Devuelve una breve descripción del servlet.
     */
    @Override
    public String getServletInfo()
    {
        return "Front Controller para la aplicación de Tienda";
    }
}