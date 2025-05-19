import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
        // Si no hay sesión, no debería haber un carrito que modificar.
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
                    } 
                    catch (NumberFormatException e) 
                    {
                        // Si indexStr no es un número válido.
                        System.err.println("AccionEliminarDelCarrito: índice inválido: " + indexStr);

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
        }

         // --- 2. REDIRECCIÓN ---

        // Después de intentar la eliminación (haya tenido éxito o no, o incluso si no había carrito),
        // SIEMPRE se redirige a la acción "verCarrito".
        response.sendRedirect(request.getContextPath() + "/app?accion=verCarrito");

        // El AppController no necesita hacer un forward.
        return null; // Indica que la respuesta ya fue manejada
    }
}