import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException; // O javax.*
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

        // --- 1. OBTENCIÓN DE PARAMETROS DE LA PETICIÓN ---

        // Obtener el email y la contraseña enviados desde el formulario de login.
        String email = request.getParameter("email_login");
        String password = request.getParameter("password_login");
        
        // --- 2. GESTIÓN DE LA SESIÓN HTTP ---

        // Obtener la sesión HTTP actual. Si no existe, se crea una nueva (gracias a true).
        HttpSession session = request.getSession(true);

        // --- 3. OBTENCIÓN DE LA INSTANCIA DE BaseDatos ---

        // Intenta obtener la instancia de BaseDatos que podría estar ya en sesión
        BaseDatos bd = (BaseDatos) session.getAttribute("bd");
        
        // Variable para almacenar la ruta del JSP al que se hará forward en caso de error.
        String vistaDestino;

        // --- 4. VALIDACIÓN BASICA DE ENTRADA ---

        // Verificar si los campos de email o contraseña están vacíos.
        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty())
        {
             System.out.println("AccionLogin: Email o contraseña vacíos.");

            // Establecer un mensaje de error específico para el login.
            request.setAttribute("errorLogin", "Email y contraseña son obligatorios.");

            // Guardar el email (si se proporcionó) para repoblar el campo en el formulario de login.
            request.setAttribute("param_email_login", email); 

            // Devolver la ruta al JSP de login para que el AppController haga forward.
            return "/login.jsp"; 
        }

        // --- 5. LÓGICA DE AUTENTICACIÓN ---
        try 
        {
            // Si la instancia de BaseDatos no estaba en sesión, se crea una nueva.
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
            if (usuario != null && usuario.getPassword().equals(password)) 
            { 
                 // --- LOGIN EXITOSO ---
                System.out.println("AccionLogin: Login exitoso para el usuario: " + email);

                // Guardar el objeto Usuario completo en la sesión.
                session.setAttribute("usuarioAutenticado", usuario); 
                
                Carrito carrito = (Carrito) session.getAttribute("carrito");

                if (carrito == null) 
                {
                    System.out.println("AccionLogin: Creando nuevo carrito para el usuario en sesión.");
                    carrito = new Carrito();
                    session.setAttribute("carrito", carrito);
                }

                // Redirigir al usuario a la página principal.
                response.sendRedirect(request.getContextPath() + "/index.jsp"); // O a /app?accion=verCarrito
                
                // Indicar al AppController que la respuesta ya fue manejada.
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