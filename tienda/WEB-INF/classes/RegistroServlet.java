import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

public class RegistroServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String tarjetaCredito = request.getParameter("tarjeta");
        HttpSession session = request.getSession();
        try{
            // Crear la conexión con al base de datos, si no existe
            BaseDatos bd = (BaseDatos) session.getAttribute("bd");
            if(bd == null){
                bd = new BaseDatos();
                session.setAttribute("bd",bd);
            }

            // Registrar usuario en BD
            Usuario usuario = new Usuario(email, password, nombre, tarjetaCredito);
            bd.agregarUsuario(usuario);

            request.setAttribute("exito", "¡Registro completado!");
            request.getRequestDispatcher("login.jsp").forward(request, response);

        } catch (SQLException e) {
            // Manejo de la excepción
            e.printStackTrace();  // Para depuración
            request.setAttribute("error", "Error de base de datos. Intenta nuevamente.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}