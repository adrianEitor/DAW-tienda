
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interfaz para representar una acción o comando a ejecutar por el controlador.
 * Cada implementación manejará la lógica de negocio específica
 * y determinará la vista a la que se debe despachar.
 */
public interface Accion {
    /**
     * Ejecuta la lógica de negocio de la acción.
     * @param request La HttpServletRequest.
     * @param response La HttpServletResponse.
     * @return Un String que representa la ruta a la vista (JSP) a la que se debe despachar,
     *         o null si la acción ya manejó la respuesta (ej. con un sendRedirect).
     * @throws ServletException Si ocurre un error de servlet.
     * @throws IOException Si ocurre un error de IO.
     */
    String ejecutar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;
}