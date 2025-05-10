import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AppController extends HttpServlet {

    // No es necesario un init() para instanciar Acciones si se crean en processRequest
    // o si usas un Factory.

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accionParam = request.getParameter("accion");
        if (accionParam == null || accionParam.trim().isEmpty()) {
            accionParam = "verIndex"; // Acción por defecto
        }

        Accion accionEjecutar = null;
        String vistaDestino = null; // Ruta al JSP

        // Selección de la Acción a ejecutar
        // Este switch podría reemplazarse por un AccionFactory para proyectos más grandes
        switch (accionParam.toLowerCase()) {
            case "agregarcd":
                accionEjecutar = new AccionAgregarAlCarrito();
                break;
            case "vercarrito":
                accionEjecutar = new AccionVerCarrito();
                break;
            case "eliminarcd":
                accionEjecutar = new AccionEliminarDelCarrito();
                break;
            case "calcularpago":
                accionEjecutar = new AccionCalcularPago();
                break;
            case "procesarpago": // Esta acción la manejará un POST desde pago.jsp
                accionEjecutar = new AccionProcesarPago();
                break;
            case "login":
                accionEjecutar = new AccionLogin();
                break;
            case "registro":
                accionEjecutar = new AccionRegistro();
                break;
            // case "logout": // Lo omitimos por ahora según tu petición
            //    accionEjecutar = new AccionLogout();
            //    break;
            case "verindex":
                vistaDestino = "/index.jsp"; // Asumiendo que está en la raíz de WebContent
                break;
            default:
                System.err.println("AppController: Acción desconocida - " + accionParam);
                request.setAttribute("error", "La acción solicitada ('" + accionParam + "') no es válida o no está implementada.");
                vistaDestino = "/WEB-INF/jsp/error.jsp"; // Un JSP de error genérico
        }

        try {
            if (accionEjecutar != null) {
                // La acción ejecutará la lógica y devolverá la ruta de la vista
                // o null si ya manejó la respuesta (ej. con sendRedirect).
                vistaDestino = accionEjecutar.ejecutar(request, response);
            }

            // Si la acción devolvió una ruta de vista y la respuesta no ha sido "committed"
            // (ej. por un sendRedirect previo en la clase Accion), entonces hacemos forward.
            if (vistaDestino != null && !response.isCommitted()) {
                NavegadorVistas.irAPagina(vistaDestino, request, response);
            }
            // Si vistaDestino es null, la clase Accion ya se encargó de la respuesta.

        } catch (Exception e) { // Captura genérica para errores inesperados en las Acciones
            e.printStackTrace(); // Loguear el error completo
            request.setAttribute("error", "Ocurrió un error inesperado en la aplicación: " + e.getMessage());
            if (!response.isCommitted()) {
                // Usar tu clase NavegadorVistas
                NavegadorVistas.irAPagina("/WEB-INF/jsp/error.jsp", request, response);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}