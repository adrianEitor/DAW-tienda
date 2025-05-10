// package com.tuproyecto.servicio;

// import com.tuproyecto.dao.BaseDatos;
// import com.tuproyecto.modelo.Usuario;
// import com.tuproyecto.modelo.Carrito; // Necesario si creas el carrito aquí
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException; // O javax.*
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AccionLogin implements Accion {

    @Override
    public String ejecutar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // LEER LOS PARÁMETROS CON LOS NUEVOS NOMBRES
        String email = request.getParameter("email_login");
        String password = request.getParameter("password_login");
        HttpSession session = request.getSession(true);

        BaseDatos bd = (BaseDatos) session.getAttribute("bd");
        String vistaDestino;

        // Validar que los parámetros no sean nulos o vacíos si son requeridos
        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("errorLogin", "Email y contraseña son obligatorios.");
            // Repoblar el campo email si se proporcionó
            request.setAttribute("param_email_login", email); // Para que el JSP lo use en value="${param_email_login}" si es necesario
            return "/login.jsp"; // O tu ruta configurada para el JSP de login
        }

        try {
            if (bd == null) {
                bd = new BaseDatos();
                session.setAttribute("bd", bd);
            }
            
            Usuario usuario = bd.obtenerUsuarioPorEmail(email);
            
            if (usuario != null && usuario.getPassword().equals(password)) { // COMPARAR HASHES EN PRODUCCIÓN
                session.setAttribute("usuarioAutenticado", usuario); // Guardar objeto Usuario completo
                
                Carrito carrito = (Carrito) session.getAttribute("carrito");
                if (carrito == null) {
                    carrito = new Carrito();
                    session.setAttribute("carrito", carrito);
                }
                response.sendRedirect(request.getContextPath() + "/index.jsp"); // O a /app?accion=verCarrito
                return null; 
            } else {
                // ESTABLECER ATRIBUTO DE ERROR ESPECÍFICO PARA LOGIN
                request.setAttribute("errorLogin", "Email o contraseña incorrectos.");
                // Repoblar el campo email para conveniencia del usuario
                request.setAttribute("param_email_login", email);
                vistaDestino = "/login.jsp"; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorLogin", "Error de base de datos. Intente nuevamente.");
             // Repoblar el campo email para conveniencia del usuario
            request.setAttribute("param_email_login", email);
            vistaDestino = "/login.jsp";
        }
        return vistaDestino;
    }
}