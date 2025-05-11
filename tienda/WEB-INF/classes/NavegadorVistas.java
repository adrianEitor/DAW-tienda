// package com.tuproyecto.util; // Si usas paquetes

import java.io.IOException;
import javax.servlet.RequestDispatcher; // O javax.* si usas Tomcat 9
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ============================================================================
 * CLASE NavegadorVistas
 * ============================================================================
 * PROPÓSITO:
 * Esta es una clase de utilidad (utility class) diseñada para centralizar y
 * simplificar la lógica de reenvío (forward) de peticiones desde un servlet
 * (típicamente el AppController) a una vista JSP.
 * Encapsula la obtención del RequestDispatcher y la llamada al método `forward`.
 * Es conceptualmente similar al método `gotoPage()` que se muestra en tus
 * diapositivas como parte de un patrón Dispatcher.
 *
 * CARACTERISTICAS DE UNA CLASE DE UTILIDAD:
 * - Generalmente contiene solo métodos estáticos.
 * - A menudo tiene un constructor privado para prevenir su instanciación, ya que
 *   no tiene sentido crear objetos de una clase que solo ofrece métodos estáticos.
 */
public class NavegadorVistas 
{

    // --- CONSTRUCTOR PRIVADO ---
    /**
     * Constructor privado para evitar que se creen instancias de esta clase de utilidad.
     * Todos sus métodos son estáticos y se deben llamar directamente usando el nombre de la clase
     * (ej. NavegadorVistas.irAPagina(...)).
     */
    private NavegadorVistas() 
    { 
        // Este constructor está vacío y es privado para reforzar el patrón de clase de utilidad. 
    }

    // --- MÉTODO ESTATICO PARA HACER FORWARD ---
    /**
     * ------------------------------------------------------------------------
     * MÉTODO irAPagina (Análogo a gotoPage)
     * ------------------------------------------------------------------------
     * Realiza un forward de la petición y respuesta actuales a la ruta JSP
     * especificada. El forward ocurre internamente en el servidor, y la URL
     * en el navegador del cliente no cambia.
     *
     * USO:
     * El AppController, después de que una clase Accion ha ejecutado su lógica
     * y ha devuelto la ruta a un JSP, llamará a este método para transferir
     * el control a ese JSP para la generación de la respuesta HTML.
     *
     * @param rutaJSP La ruta al archivo JSP de destino. Debe ser una ruta relativa
     *                al contexto de la aplicación.
     *                Ejemplos:
     *                - "/miPagina.jsp" (si el JSP está en la raíz de WebContent)
     *                - "/WEB-INF/jsp/vistaSegura.jsp" (si el JSP está protegido dentro de WEB-INF)
     *
     * @param request La HttpServletRequest actual que se reenviará al JSP.
     *                El JSP tendrá acceso a los atributos establecidos en este request.
     *
     * @param response La HttpServletResponse actual. El control sobre esta respuesta
     *                 se cede al JSP. No se debe haber escrito nada en el cuerpo de la
     *                 respuesta antes de llamar a forward().
     *
     * @throws ServletException Si ocurre un error durante la operación de forward
     *                          (por ejemplo, si el JSP no se encuentra o tiene un error).
     *
     * @throws IOException Si ocurre un error de entrada/salida durante la operación
     *                     de forward.
     */
    public static void irAPagina(String rutaJSP, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // --- VALIDACIÓN DE LA RUTA DEL JSP ---

        // Es una buena práctica verificar que la ruta proporcionada no sea nula o vacía.
        if (rutaJSP == null || rutaJSP.trim().isEmpty()) 
        {
            // Manejar error de ruta no especificada, por ejemplo, enviando a una página de error general
            System.err.println("NavegadorVistas: La ruta del JSP no puede ser nula o vacía.");

            // Se envía un error HTTP 500 (Error Interno del Servidor) al cliente.
            // Esto es mejor que dejar que la aplicación falle con una NullPointerException
            // si se intenta obtener un RequestDispatcher con una ruta nula.
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno: Vista no especificada.");
            
            // Se retorna para evitar continuar con una ruta inválida.
            return;
        }
        
        // --- OBTENCIÓN DEL RequestDispatcher Y REALIZACIÓN DEL FORWARD ---
        // 1. Obtener un objeto RequestDispatcher para el recurso especificado (rutaJSP).
        //    El RequestDispatcher es el mecanismo que permite a un servlet delegar
        //    una petición a otro recurso en el servidor.
        //    request.getRequestDispatcher(rutaJSP) obtiene el dispatcher relativo a la
        //    petición actual, lo cual es lo usual para forwards a JSPs.
        RequestDispatcher dispatcher = request.getRequestDispatcher(rutaJSP);

        // 2. Utilizar el método forward() del dispatcher para transferir el control.
        //    Esto incluye los objetos request y response actuales al recurso de destino (el JSP).
        //    El JSP entonces procesará la petición y generará la respuesta.
        //    Importante: El forward debe ser la última acción en el flujo de respuesta;
        //    no se debe escribir más en el response después de esta llamada.

        System.out.println("NavegadorVistas: Haciendo forward a: " + rutaJSP); // Log para depuración

        dispatcher.forward(request, response);
    }
}