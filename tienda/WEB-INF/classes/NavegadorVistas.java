import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ============================================================================
 * CLASE NavegadorVistas
 * ============================================================================
 * PROPÓSITO:
 * Esta es una clase de utilidad  diseñada para centralizar y
 * simplificar la lógica de reenvío (forward) de peticiones desde un servlet
 * (típicamente el AppController) a una vista JSP.
 */
public class NavegadorVistas 
{

    // --- CONSTRUCTOR PRIVADO ---
    private NavegadorVistas() 
    { 
         
    }

    // --- MÉTODO ESTATICO PARA HACER FORWARD ---
    /**
     * ------------------------------------------------------------------------
     * MÉTODO irAPagina 
     * ------------------------------------------------------------------------
     * Realiza un forward de la petición y respuesta actuales a la ruta JSP
     * especificada. El forward ocurre internamente en el servidor, y la URL
     * en el navegador del cliente no cambia.
     *
     * USO:
     * El AppController, después de que una clase Accion ha ejecutado su lógica
     * y ha devuelto la ruta a un JSP, llamará a este método para transferir
     * el control a ese JSP para la generación de la respuesta HTML.
     *
     * @param rutaJSP La ruta al archivo JSP de destino. Debe ser una ruta relativa
     *                al contexto de la aplicación.
     *
     * @param request La HttpServletRequest actual que se reenviará al JSP.
     *
     * @param response La HttpServletResponse actual. El control sobre esta respuesta
     *                 se cede al JSP. 
     *     
     * @throws ServletException Si ocurre un error durante la operación de forward
     *
     * @throws IOException Si ocurre un error de entrada/salida durante la operación
     *                     de forward.
     */
    public static void irAPagina(String rutaJSP, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // --- VALIDACIÓN DE LA RUTA DEL JSP ---

        // Verificar que la ruta proporcionada no sea nula o vacía.
        if (rutaJSP == null || rutaJSP.trim().isEmpty()) 
        {
            // Manejar error de ruta no especificada, por ejemplo, enviando a una página de error general
            System.err.println("NavegadorVistas: La ruta del JSP no puede ser nula o vacía.");

            return;
        }
        
        // --- OBTENCIÓN DEL RequestDispatcher Y REALIZACIÓN DEL FORWARD ---
        // 1. Obtener un objeto RequestDispatcher para el recurso especificado (rutaJSP).
        RequestDispatcher dispatcher = request.getRequestDispatcher(rutaJSP);

        // 2. Utilizar el método forward() del dispatcher para transferir el control.
        System.out.println("NavegadorVistas: Haciendo forward a: " + rutaJSP); // Log para depuración
        dispatcher.forward(request, response);
    }
}