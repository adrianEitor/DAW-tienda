import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AccionVerCarrito implements Accion 
{

    /**
     * ------------------------------------------------------------------------
     * MÉTODO ejecutar (Implementación de la interfaz Accion)
     * ------------------------------------------------------------------------
     * Obtiene el carrito de la sesión (o crea uno vacío si no existe)
     * y lo prepara para ser mostrado por la vista JSP.
     */
    @Override
    public String ejecutar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        System.out.println("AccionVerCarrito: Iniciando preparación para ver el carrito."); // Log para depuración

        // --- 1. OBTENCIÓN DE LA SESIÓN HTTP ---

        // Intenta obtener la sesión HTTP existente. request.getSession(false) devuelve
        // null si no hay una sesión activa; no crea una nueva.
        HttpSession session = request.getSession(false);

        Carrito carrito = null; // Inicializa la referencia al objeto Carrito.

        // --- 2. OBTENCIÓN DEL CARRITO DE LA SESIÓN ---
        // Solo intentar obtener el carrito si existe una sesión.
        if (session != null) 
        {
            System.out.println("AccionVerCarrito: Sesión encontrada. Intentando obtener 'carrito'.");
            // Intentar obtener el objeto Carrito que podría estar almacenado en la sesión.
            // La clave "carrito" debe ser la misma utilizada en otras acciones (ej., AccionAgregarAlCarrito).
            carrito = (Carrito) session.getAttribute("carrito");
            
            if (carrito != null) 
            {
                System.out.println("AccionVerCarrito: Carrito encontrado en sesión.");
            } 
            else 
            {
                System.out.println("AccionVerCarrito: Atributo carrito no encontrado en sesión.");
            }

        }
        else 
        {
            System.out.println("AccionVerCarrito: No hay sesión activa.");
        }

         // --- 3. MANEJO DE CARRITO INEXISTENTE ---

        // Si no se encontró un objeto Carrito en la sesión (ya sea porque la sesión era null
        // o porque el atributo "carrito" no estaba en la sesión), se crea una nueva
        // instancia de Carrito (que estará vacía).
        // Esto asegura que la vista JSP siempre reciba un objeto Carrito válido
        // y no tenga que lidiar con un valor null, simplificando la lógica en el JSP.
        if (carrito == null) 
        {
            System.out.println("AccionVerCarrito: Carrito es null, creando una nueva instancia vacía.");

            carrito = new Carrito();
        }
        
        // --- 4. PREPARACIÓN DE DATOS PARA LA VISTA (JSP) ---
        // Poner el objeto Carrito (ya sea el recuperado de la sesión o el nuevo y vacío)
        // como un atributo en el objeto request.
        // El JSP verCarrito.jsp podrá acceder a este objeto usando EL: ${carrito}.
        request.setAttribute("carrito", carrito);
        
         // --- 5. DEVOLVER RUTA DE LA VISTA ---
        // Devolver la ruta al archivo JSP que mostrará el contenido del carrito.
        // El AppController usará esta cadena para realizar el RequestDispatcher.forward().
        
        String vistaDestino = "/verCarrito.jsp"; 
        
        System.out.println("AccionVerCarrito: Devolviendo ruta a la vista: " + vistaDestino);
        
        return vistaDestino;
    }
}