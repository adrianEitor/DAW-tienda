import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException; // O javax.*
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * ============================================================================
 * CLASE AccionLogin
 * ============================================================================
 * IMPLEMENTA: Interfaz Accion
 *
 * PROPÓSITO:
 * Esta clase se encarga de procesar la petición de inicio de sesión de un usuario.
 * Valida las credenciales (email y contraseña) proporcionadas contra la base de datos.
 * Si el login es exitoso, establece el usuario autenticado en la sesión HTTP
 * y redirige al usuario a la página principal (o a otra página designada).
 * Si el login falla, prepara un mensaje de error y reenvía al usuario
 * de vuelta a la página de login para que lo intente de nuevo.
 *
 * Es invocada por el AppController cuando el parámetro "accion" es "login".
 */
public class AccionLogin implements Accion 
{

    /**
     * ------------------------------------------------------------------------
     * MÉTODO ejecutar (Implementación de la interfaz Accion)
     * ------------------------------------------------------------------------
     * Contiene la lógica principal para el proceso de inicio de sesión.
     */
    @Override
    public String ejecutar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        
         System.out.println("AccionLogin: Iniciando proceso de login."); // Log para depuración

        // --- 1. OBTENCIÓN DE PARÁMETROS DE LA PETICIÓN ---

        // Obtener el email y la contraseña enviados desde el formulario de login.
        // Se espera que el formulario en login.jsp use name="email_login" y name="password_login".
        String email = request.getParameter("email_login");
        String password = request.getParameter("password_login");
        
        // --- 2. GESTIÓN DE LA SESIÓN HTTP ---

        // Obtener la sesión HTTP actual. Si no existe, se crea una nueva (gracias a true).
        // La sesión se usará para almacenar el objeto Usuario si el login es exitoso,
        // y también para obtener/guardar la instancia de BaseDatos.
        HttpSession session = request.getSession(true);

        // --- 3. OBTENCIÓN DE LA INSTANCIA DE BaseDatos ---

        // Intenta obtener la instancia de BaseDatos que podría estar ya en sesión
        // (por ejemplo, si el usuario ya interactuó con RegistroServlet).
        BaseDatos bd = (BaseDatos) session.getAttribute("bd");
        
        // Variable para almacenar la ruta del JSP al que se hará forward en caso de error.
        String vistaDestino;

        // --- 4. VALIDACIÓN BÁSICA DE ENTRADA ---

        // Verificar si los campos de email o contraseña están vacíos.
        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty())
        {
             System.out.println("AccionLogin: Email o contraseña vacíos.");

            // Establecer un mensaje de error específico para el login.
            request.setAttribute("errorLogin", "Email y contraseña son obligatorios.");

            // Guardar el email (si se proporcionó) para repoblar el campo en el formulario de login.
            request.setAttribute("param_email_login", email); 

            // Devolver la ruta al JSP de login para que el AppController haga forward.
            return "/login.jsp"; // Asegúrate que esta ruta es correcta.
        }

        // --- 5. LÓGICA DE AUTENTICACIÓN ---
        try 
        {
            // Si la instancia de BaseDatos no estaba en sesión, se crea una nueva.
            // ¡Considerar las implicaciones de tener una conexión a BD por sesión! (Antipatrón)
            if (bd == null) 
            {
                System.out.println("AccionLogin: Instancia de BaseDatos no encontrada en sesión, creando una nueva.");

                bd = new BaseDatos();

                // Guardar la nueva instancia en sesión.
                session.setAttribute("bd", bd);
            }
            
            // Intentar obtener el usuario de la base de datos usando el email proporcionado.
            Usuario usuario = bd.obtenerUsuarioPorEmail(email);
            
            // Validar las credenciales.
            // IMPORTANTE: En un entorno de producción, NUNCA guardar ni comparar contraseñas
            // en texto plano. Deberías usar una función de hash segura.
            // para almacenar las contraseñas y para comparar la contraseña ingresada.
            if (usuario != null && usuario.getPassword().equals(password)) 
            { 
                 // --- LOGIN EXITOSO ---
                System.out.println("AccionLogin: Login exitoso para el usuario: " + email);

                // Guardar el objeto Usuario completo en la sesión.
                // Esto permite acceder a la información del usuario (nombre, id, etc.) en otras partes de la aplicación.
                session.setAttribute("usuarioAutenticado", usuario); // Guardar objeto Usuario completo
                
                // Opcional: Crear o asegurar que exista un objeto Carrito en sesión para este usuario.
                Carrito carrito = (Carrito) session.getAttribute("carrito");

                if (carrito == null) 
                {
                    System.out.println("AccionLogin: Creando nuevo carrito para el usuario en sesión.");
                    carrito = new Carrito();
                    session.setAttribute("carrito", carrito);
                }

                // Redirigir al usuario a la página principal (o a la vista del carrito, o a donde sea apropiado).
                // Usar sendRedirect sigue el patrón Post/Redirect/Get.
                response.sendRedirect(request.getContextPath() + "/index.jsp"); // O a /app?accion=verCarrito
                
                // Indicar al AppController que la respuesta ya fue manejada (sendRedirect).
                return null; 
            } 
            else 
            {
                // --- LOGIN FALLIDO (Credenciales incorrectas o usuario no existe) ---
                System.out.println("AccionLogin: Login fallido para el email: " + email);

                // Establecer un mensaje de error específico para el login.
                request.setAttribute("errorLogin", "Email o contraseña incorrectos.");

                // Guardar el email ingresado para repoblar el campo en el formulario.
                request.setAttribute("param_email_login", email);

                vistaDestino = "/login.jsp"; // Ruta al JSP de login para mostrar el error.
            }
        } 
        catch (SQLException e) 
        {
            // --- ERROR DE BASE DE DATOS ---
            System.err.println("AccionLogin: SQLException durante el login: " + e.getMessage());
            e.printStackTrace(); // Imprimir la traza completa del error en la consola de Tomcat.

            // Establecer un mensaje de error genérico de base de datos.
            request.setAttribute("errorLogin", "Error de base de datos al intentar iniciar sesión. Intente nuevamente.");
            
            // Guardar el email ingresado para repoblar el campo.
            request.setAttribute("param_email_login", email);

            vistaDestino = "/login.jsp"; // Ruta al JSP de login.
        }
        
        // Devolver la ruta del JSP al que se debe hacer forward (en caso de error de login o BD).
        return vistaDestino;
    }
}