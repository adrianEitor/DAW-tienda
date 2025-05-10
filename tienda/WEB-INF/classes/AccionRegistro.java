// package com.tuproyecto.servicio;

// import com.tuproyecto.dao.BaseDatos;
// import com.tuproyecto.modelo.Usuario;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException; // O javax.*
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AccionRegistro implements Accion {

    @Override
    public String ejecutar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // LEER LOS PARÁMETROS CON LOS NUEVOS NOMBRES
        String nombre = request.getParameter("nombre_reg");
        String email = request.getParameter("email_reg");
        String password = request.getParameter("password_reg");
        String tarjetaCredito = request.getParameter("tarjeta_reg");
        HttpSession session = request.getSession(true);

        BaseDatos bd = (BaseDatos) session.getAttribute("bd");
        String vistaDestino;

        // Validaciones básicas (puedes añadir más)
        if (nombre == null || nombre.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.isEmpty()) {
            
            request.setAttribute("errorRegistro", "Nombre, email y contraseña son obligatorios.");
            // Repoblar campos para conveniencia
            request.setAttribute("param_nombre_reg", nombre);
            request.setAttribute("param_email_reg", email);
            request.setAttribute("param_tarjeta_reg", tarjetaCredito);
            return "/login.jsp"; // Vuelve a la página con el formulario de registro
        }

        try {
            if (bd == null) {
                bd = new BaseDatos();
                session.setAttribute("bd", bd);
            }
            
            Usuario nuevoUsuario = new Usuario(email, password, nombre, tarjetaCredito); // Usa tu constructor
            
            bd.agregarUsuario(nuevoUsuario);

            // ESTABLECER ATRIBUTO DE ÉXITO ESPEĆIFICO PARA REGISTRO
            request.setAttribute("exitoRegistro", "¡Registro completado! Por favor, inicia sesión.");
            vistaDestino = "/login.jsp"; 

        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getMessage().toLowerCase().contains("unique constraint") || e.getMessage().toLowerCase().contains("duplicate key")) {
                // ESTABLECER ATRIBUTO DE ERROR ESPECÍFICO PARA REGISTRO
                request.setAttribute("errorRegistro", "El email '" + email + "' ya está registrado.");
            } else {
                request.setAttribute("errorRegistro", "Error de base de datos durante el registro.");
            }
            // Repoblar campos para conveniencia
            request.setAttribute("param_nombre_reg", nombre);
            request.setAttribute("param_email_reg", email);
            request.setAttribute("param_tarjeta_reg", tarjetaCredito);
            vistaDestino = "/login.jsp"; // Vuelve a la página con el formulario de registro
        }
        return vistaDestino;
    }
}