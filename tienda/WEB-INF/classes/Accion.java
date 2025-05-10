
import java.io.IOException;

// Importa la clase ServletException para manejar errores generales
// específicos de la tecnología Servlet. Por ejemplo, si un forward falla.
import javax.servlet.ServletException;

// Importa la interfaz HttpServletRequest, que representa la petición HTTP
// hecha por el cliente al servidor. Contiene información como parámetros,
// cabeceras, cookies, y permite establecer atributos.
import javax.servlet.http.HttpServletRequest;

// Importa la interfaz HttpServletResponse, que representa la respuesta HTTP
// que el servidor enviará de vuelta al cliente. Permite establecer
// cabeceras, el tipo de contenido, y escribir el cuerpo de la respuesta.
import javax.servlet.http.HttpServletResponse;

/**
 * ============================================================================
 * INTERFAZ Accion
 * ============================================================================
 * PROPÓSITO:
 * Esta interfaz define un contrato para todas las acciones o comandos que
 * la aplicación puede ejecutar en respuesta a una petición del usuario.
 * Es una pieza central del patrón de diseño Front Controller (en tu caso, AppController)
 * y del patrón Command (donde cada acción concreta es un comando).
 *
 * FUNCIONAMIENTO EN EL PATRÓN MVC (Model-View-Controller) / SERVICE TO WORKER:
 * 
 * 1. El AppController (Front Controller) recibe una petición HTTP.
 * 
 * 2. Determina qué acción específica se ha solicitado (ej., "agregarCD", "verCarrito").
 * 3. Crea o localiza una instancia de una clase que implementa esta interfaz Accion
 *    (por ejemplo, AccionAgregarAlCarrito, AccionVerCarrito). Estas clases
 *    son los "Workers" o "Helpers" que contienen la lógica de negocio.
 * 
 * 4. El AppController invoca el método ejecutar() de la instancia de Accion seleccionada.
 * 
 * 5. La clase Accion concreta:
 *    a. Realiza la lógica de negocio (interactúa con el Modelo: JavaBeans, DAOs).
 * 
 *    b. Prepara los datos que la Vista (JSP) necesitará (generalmente poniéndolos
 *       como atributos en el objeto request).
 * 
 *    c. Decide a qué Vista (JSP) se debe redirigir o hacer forward.
 *
 * VENTAJA DE USAR UNA INTERFAZ:
 * - El AppController solo necesita conocer la interfaz Accion,
 *   no los detalles de implementación de cada acción específica. Esto hace que el
 *   AppController sea más genérico y fácil de mantener.
 * 
 * - Para añadir una nueva funcionalidad a la aplicación, solo
 *   necesitas crear una nueva clase que implemente esta interfaz Accion y
 *   añadir un caso al AppController para que la invoque. No necesitas modificar
 *   la estructura central del controlador para cada nueva acción.
 * 
 * - El AppController puede tratar a todas las diferentes clases de acción
 *   de manera uniforme a través de la referencia de tipo Accion.
 *
 * Cada implementación de esta interfaz:
 * 
 *  - Manejará la lógica de negocio específica para una solicitud particular.
 * 
 *  - Preparará los datos necesarios para la vista (si es que hay una vista a la que hacer forward).
 * 
 *  - Determinará la ruta a la siguiente vista (JSP) a la que se debe despachar la petición,
 *    o manejará la respuesta completamente (por ejemplo, realizando un sendRedirect).
 */

// public para que sea accesible desde otros paquetes (como el controlador)
public interface Accion 
{
        /**
     * ------------------------------------------------------------------------
     * MÉTODO ejecutar
     * ------------------------------------------------------------------------
     * Este es el único método definido por la interfaz Accion. Cada clase
     * que implemente Accion deberá proporcionar una implementación concreta
     * de este método.
     *
     * RESPONSABILIDADES DEL MÉTODO EN UNA CLASE CONCRETA:
     * 
     * 1. Obtener parámetros de la petición (request.getParameter()).
     * 
     * 2. Interactuar con la lógica de negocio (Modelo):
     *    - Obtener o crear objetos JavaBean (ej., Carrito).
     *    - Llamar a métodos de clases de servicio o DAOs si es necesario.
     *    - Manipular datos (ej., agregar un CD al carrito).
     * 3. Preparar datos para la Vista:
     *    - Colocar objetos o resultados en el request scope (request.setAttribute())
     *      o en el session scope (`session.setAttribute()) para que el JSP
     *      pueda acceder a ellos usando Expression Language (EL).
     * 4. Determinar el flujo de navegación:
     *    - Devolver la ruta (String) al archivo JSP al que se debe hacer forward.
     *    - O, si la acción realiza una redirección (response.sendRedirect()),
     *      este método debe devolver null para indicar al AppController que
     *      la respuesta ya ha sido gestionada y no se necesita hacer forward.
     *
     * @param request  La HttpServletRequest que encapsula la información de la
     *                 petición HTTP del cliente. Proporciona acceso a parámetros,
     *                 cabeceras, la sesión, etc.
     *
     * @param response La HttpServletResponse que se utilizará para enviar la
     *                 respuesta HTTP de vuelta al cliente. Permite establecer el
     *                 tipo de contenido, cabeceras, y escribir datos en el cuerpo
     *                 de la respuesta, o realizar redirecciones.
     *
     * @return Un String que representa la ruta al recurso (generalmente un archivo JSP)
     *         al que el AppController debe hacer forward. La ruta debe ser
     *         relativa al contexto de la aplicación (ej., "/pagina.jsp").
     *         Si la acción ya ha manejado completamente la respuesta (por ejemplo,
     *         mediante una llamada a response.sendRedirect()), este método
     *         DEBE devolver null.
     *
     * @throws ServletException Si ocurre un error específico del servlet durante
     *                          el procesamiento de la acción que impide la continuación
     *                          normal del flujo. El AppController podría capturar esto.
     *
     * @throws IOException Si ocurre un error de entrada/salida, por ejemplo, al
     *                     intentar escribir en el response o si sendRedirect falla.
     *                     El AppController podría capturar esto.
     */
    String ejecutar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}