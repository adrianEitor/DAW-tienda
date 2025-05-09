import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        HttpSession session = request.getSession(true);

        try {
            // Crear la conexión con la base de datos, si no existe
            BaseDatos bd = (BaseDatos) session.getAttribute("bd");
            if(bd == null){
                bd = new BaseDatos();
                session.setAttribute("bd", bd);
            }
            // Validar credenciales en la base de datos
            Usuario usuario = bd.obtenerUsuarioPorEmail(email);
            if (usuario != null && usuario.getEmail().equals(email) && usuario.getPassword().equals(password)) {
                session.setAttribute("usuario", email); // Guardar usuario en sesión
                response.sendRedirect("VerCarritoServlet"); // Redirigir al carrito
            } else {
                request.setAttribute("error", "Credenciales incorrectas");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            // Manejo de la excepción
            e.printStackTrace();  // Para depuración
            request.setAttribute("error", "Error de base de datos. Intenta nuevamente.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }        
    }
}
