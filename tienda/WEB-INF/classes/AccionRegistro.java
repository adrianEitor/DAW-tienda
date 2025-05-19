import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException; // O javax.*
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AccionRegistro implements Accion 
{

    /**
     * ------------------------------------------------------------------------
     * MÉTODO ejecutar (Implementación de la interfaz Accion)
     * ------------------------------------------------------------------------
     * Contiene la lógica principal para el proceso de registro de un nuevo usuario.
     */
    @Override
    public String ejecutar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        
        System.out.println("AccionRegistro: Iniciando proceso de registro."); // Log para depuración

        // --- 1. OBTENCIÓN DE PARAMETROS DEL FORMULARIO DE REGISTRO ---

        // Se leen los parámetros enviados desde el formulario en registro.jsp (o login.jsp si está combinado).
        // Se asume que los campos del formulario tienen los names: "nombre_reg", "email_reg", "password_reg", "tarjeta_reg".
        String nombre = request.getParameter("nombre_reg");
        String email = request.getParameter("email_reg");
        String password = request.getParameter("password_reg"); // ¡IMPORTANTE: Considerar hashear esta contraseña antes de guardarla!
        String tarjetaCredito = request.getParameter("tarjeta_reg");
       
        // --- 2. GESTIÓN DE LA SESIÓN HTTP Y BaseDatos ---

        // Obtener la sesión HTTP actual. Si no existe, se crea una nueva (gracias a true).
        // La sesión se usa para obtener/guardar la instancia de BaseDatos.
        HttpSession session = request.getSession(true);

        BaseDatos bd = (BaseDatos) session.getAttribute("bd");
        String vistaDestino; // Variable para la ruta del JSP al que se hará forward.

         // --- 3. VALIDACIÓN BASICA DE LOS DATOS DE ENTRADA ---
        // Comprobar que los campos obligatorios (nombre, email, contraseña) no estén vacíos.
        // Se pueden añadir validaciones más complejas (formato de email, fortaleza de contraseña, etc.).
        if (nombre == null || nombre.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.isEmpty()) 
            {
            System.out.println("AccionRegistro: Campos obligatorios vacíos.");
            // Establecer un mensaje de error específico para el registro.
            request.setAttribute("errorRegistro", "Nombre, email y contraseña son obligatorios.");
            
            // Repoblar los campos del formulario para que el usuario no tenga que reescribir todo.
            // Estos atributos serán leídos por el JSP en los atributos value de los inputs.
            request.setAttribute("param_nombre_reg", nombre);
            request.setAttribute("param_email_reg", email);
            request.setAttribute("param_tarjeta_reg", tarjetaCredito);  // También repoblar campos opcionales si se ingresaron.
            
            // Devolver la ruta al JSP de login
            // para que el AppController haga forward y muestre el error y los datos.
            return "/login.jsp"; // Vuelve a la página con el formulario de registro
        }

        // --- 4. LÓGICA DE REGISTRO DEL USUARIO ---
        try 
        {
            // Si la instancia de BaseDatos no estaba en sesión, se crea una nueva.
            if (bd == null) 
            {
                bd = new BaseDatos();
                session.setAttribute("bd", bd); // Guardar la nueva instancia en sesión.
            }
            
            // Crear un nuevo objeto Usuario utilizando el constructor que recibe los parámetros.
            Usuario nuevoUsuario = new Usuario(email, password, nombre, tarjetaCredito); // Usa tu constructor
            
             System.out.println("AccionRegistro: Intentando agregar usuario a la BD: " + email);

            // Llamar al método de BaseDatos (que a su vez llama a UsuariosDAO) para agregar el usuario.
            bd.agregarUsuario(nuevoUsuario);

            System.out.println("AccionRegistro: Usuario agregado a la BD con éxito: " + email);

            // --- REGISTRO EXITOSO ---
            // Establecer un mensaje de éxito específico para el registro.
            // Este mensaje se mostrará en la página de login.
            request.setAttribute("exitoRegistro", "¡Registro completado! Por favor, inicia sesión con sus nuevas credenciales.");
            
            vistaDestino = "/login.jsp"; 

        } 
        catch (SQLException e)
        {
              // --- ERROR DURANTE EL REGISTRO (generalmente un error de BD) ---
            System.err.println("AccionRegistro: SQLException durante el registro: " + e.getMessage());
            e.printStackTrace(); // Imprimir la traza completa del error en la consola de Tomcat.

            // Comprobar si el error es debido a una violación de restricción única (ej. email duplicado).
            // El mensaje exacto puede variar según el SGBD (PostgreSQL en tu caso).
            if ( e.getMessage().toLowerCase().contains("unique constraint") || e.getMessage().toLowerCase().contains("duplicate key") ||
                                           e.getMessage().toLowerCase().contains("ya existe la llave") ) 
            {
                // ESTABLECER ATRIBUTO DE ERROR ESPECIFICO PARA REGISTRO
                 request.setAttribute("errorRegistro", "El email '" + email + "' ya está registrado. Por favor, use otro email o inicie sesión.");
            }
            else 
            {
                request.setAttribute("errorRegistro", "Error de base de datos durante el registro.");
            }

              // Repoblar los campos del formulario para que el usuario no tenga que reescribir.
            request.setAttribute("param_nombre_reg", nombre);
            request.setAttribute("param_email_reg", email);
            request.setAttribute("param_tarjeta_reg", tarjetaCredito);

            vistaDestino = "/login.jsp"; // Vuelve a la página con el formulario de registro
        }

        return vistaDestino;
    }
}