// package com.tuproyecto.util; // Si usas paquetes

import java.io.IOException;
import javax.servlet.RequestDispatcher; // O javax.* si usas Tomcat 9
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NavegadorVistas {

    // Constructor privado para que no se pueda instanciar (clase de utilidad)
    private NavegadorVistas() {}

    /**
     * Reenvía la petición y respuesta a la ruta JSP especificada.
     * Este método es análogo al "gotoPage" de las diapositivas.
     * 
     * @param rutaJSP La ruta al archivo JSP (ej. "/WEB-INF/jsp/miVista.jsp").
     * @param request La HttpServletRequest actual.
     * @param response La HttpServletResponse actual.
     * @throws ServletException Si ocurre un error durante el forward.
     * @throws IOException Si ocurre un error de IO durante el forward.
     */
    public static void irAPagina(String rutaJSP, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if (rutaJSP == null || rutaJSP.trim().isEmpty()) {
            // Manejar error de ruta no especificada, por ejemplo, enviando a una página de error general
            System.err.println("NavegadorVistas: La ruta del JSP no puede ser nula o vacía.");
            // Podrías lanzar una excepción aquí o redirigir a una página de error por defecto
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno: Vista no especificada.");
            return;
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher(rutaJSP);
        dispatcher.forward(request, response);
    }
}