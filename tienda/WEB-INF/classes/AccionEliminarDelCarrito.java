import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// -----------------------------------------------------------------------------
// CLASE AccionEliminarDelCarrito
// -----------------------------------------------------------------------------

/**
 * ============================================================================
 * CLASE AccionEliminarDelCarrito
 * ============================================================================
 * IMPLEMENTA: Interfaz Accion
 *
 * PROPÓSITO:
 * Esta clase maneja la lógica de negocio para eliminar un ítem (o una unidad de un ítem)
 * del carrito de la compra del usuario.
 * Es invocada por el AppController cuando el parámetro "accion" es "eliminarCD".
 *
 * FUNCIONAMIENTO:
 * 1. Intenta obtener la sesión HTTP existente del usuario (no crea una nueva si no existe).
 * 
 * 2. Si la sesión existe, intenta obtener el objeto Carrito de la sesión.
 * 
 * 3. Si el Carrito existe, obtiene el parámetro "index" de la petición, que indica
 *    la posición del ítem a eliminar/modificar en la lista de CDs del carrito.
 * 
 * 4. Convierte el "index" a un entero.
 * 
 * 5. Llama al método eliminarItem(index) del objeto Carrito. Este método
 *    se encargará de decrementar la cantidad del CD en esa posición o de
 *    eliminarlo completamente si la cantidad llega a cero.
 * 
 * 6. Maneja posibles errores, como un "index" inválido (que no sea un número).
 * 
 * 7. Después de intentar la eliminación, SIEMPRE realiza una redirección (sendRedirect)
 *    a la acción "verCarrito" (manejada por AppController). Esto es para que el
 *    usuario vea el estado actualizado del carrito y para seguir el patrón PRG.
 * 
 * 8. Devuelve null para indicar al AppController que la respuesta ya fue manejada.
 */
public class AccionEliminarDelCarrito implements Accion 
{
    /**
     * ------------------------------------------------------------------------
     * MÉTODO ejecutar (Implementación de la interfaz Accion)
     * ------------------------------------------------------------------------
     * Contiene la lógica principal para eliminar un ítem del carrito.
     */
    @Override
    public String ejecutar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        
        System.out.println("AccionEliminarDelCarrito: Iniciando eliminación de ítem."); // Log para depuración

        // --- 1. OBTENCIÓN DE LA SESIÓN Y EL CARRITO ---

        // Intenta obtener la sesión HTTP existente. request.getSession(false) devuelve
        // null si no hay una sesión activa para el cliente; no crea una nueva.
        // Esto es apropiado porque si no hay sesión, no debería haber un carrito que modificar.
        HttpSession session = request.getSession(false);
        
        // Solo proceder si existe una sesión.
        if (session != null) 
        {
            System.out.println("AccionEliminarDelCarrito: Sesión encontrada.");

            // Intentar obtener el objeto Carrito de la sesión.
            Carrito carrito = (Carrito) session.getAttribute("carrito");

            // Solo proceder si el objeto Carrito existe en la sesión.
            if (carrito != null) 
            {
                System.out.println("AccionEliminarDelCarrito: Carrito encontrado en sesión.");

                // Obtener el parámetro index de la URL de la petición.
                // Este index indica la posición del CD a eliminar en la lista de ítems del carrito.
                // Ejemplo de URL: /app?accion=eliminarCD&index=0
                String indexStr = request.getParameter("index");

                // Solo proceder si se proporcionó el parámetro index.
                if (indexStr != null) 
                {
                    try 
                    {
                        // Convertir la cadena del índice a un entero.
                        int index = Integer.parseInt(indexStr);

                        // Llamar al método eliminarItem del objeto Carrito
                        // para realizar la lógica de eliminación o decremento de cantidad.
                        carrito.eliminarItem(index);

                        // OPCIONAL: Volver a guardar el objeto carrito en la sesión.
                        // session.setAttribute("carrito", carrito);
                        // Aunque el objeto carrito obtenido de la sesión es modificado por referencia
                        // (su lista interna de 'items' cambia).
                    } 
                    catch (NumberFormatException e) 
                    {
                        // Si indexStr no es un número válido.
                        System.err.println("AccionEliminarDelCarrito: Índice inválido: " + indexStr);

                        // Se podría guardar un mensaje de error en el request si se hiciera forward,
                        // pero como hacemos redirect, el mensaje se perdería.
                        // La redirección a verCarrito simplemente mostrará el carrito sin cambios
                        // si el índice fue inválido y no se pudo procesar la eliminación.
                        request.setAttribute("errorCarrito", "No se pudo eliminar el ítem, índice inválido.");
                    }
                }
                else 
                {
                    System.err.println("AccionEliminarDelCarrito: No se proporcionó el parámetro 'index'.");
                    request.setAttribute("errorCarrito", "No se especificó qué ítem eliminar.");
                }
            }
            else 
            {
                System.out.println("AccionEliminarDelCarrito: No se encontró el atributo 'carrito' en sesión.");
            }
        }
        else 
        {
            System.out.println("AccionEliminarDelCarrito: No hay sesión activa. No se puede eliminar del carrito.");
            // Si no hay sesión, no hay carrito que modificar. La redirección simplemente mostrará
            // un carrito vacío o la página de inicio si verCarrito lo maneja así.
        }

         // --- 2. REDIRECCIÓN ---

        // Después de intentar la eliminación (haya tenido éxito o no, o incluso si no había carrito),
        // SIEMPRE se redirige a la acción "verCarrito".
        // Esto asegura que el usuario vea el estado más reciente del carrito.
        // Sigue el patrón Post/Redirect/Get (aunque esta acción es GET, la redirección
        // asegura una URL limpia y una recarga segura).
        response.sendRedirect(request.getContextPath() + "/app?accion=verCarrito");

        // Se devuelve null porque la respuesta ya ha sido manejada por sendRedirect().
        // El AppController no necesita hacer un forward.
        return null; // Indica que la respuesta ya fue manejada
    }
}