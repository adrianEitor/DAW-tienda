import java.io.IOException;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AccionAgregarAlCarrito implements Accion 
{

    /**
     * ------------------------------------------------------------------------
     * MÉTODO ejecutar (Implementación de la interfaz Accion)
     * ------------------------------------------------------------------------
     * Contiene la lógica principal para agregar un CD al carrito.
     */
    @Override
    public String ejecutar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // --- 1. GESTIÓN DE LA SESIÓN Y EL CARRITO ---
        // Obtener la sesión HTTP actual. Si no existe, se crea una nueva (gracias a true).
        HttpSession session = request.getSession(true);

        // Intentar obtener el objeto Carrito que podría estar ya almacenado en la sesión.
        // La clave "carrito" debe ser la misma que se usa en todas partes para este objeto.
        Carrito carrito = (Carrito) session.getAttribute("carrito");

        // Si no se encontró ningún objeto Carrito en la sesión (ej. es la primera vez
        // que el usuario añade algo, o la sesión expiró y se creó una nueva),
        // entonces se crea una nueva instancia del JavaBean Carrito.
        if (carrito == null)
        {
            // Llama al constructor por defecto de Carrito.
            carrito = new Carrito();

            // Guardar el nuevo objeto Carrito en la sesión para futuras peticiones.
            session.setAttribute("carrito", carrito);
        }
        
        // --- 2. OBTENCION DE PARAMETROS DE LA PETICION ---

        // Obtener la cadena que representa el CD seleccionado del formulario (del <select name="cd">).
        String cdSeleccionado = request.getParameter("cd");

        // Obtener la cadena que representa la cantidad del formulario (del <input name="cantidad">).
        String cantidadStr = request.getParameter("cantidad");

        // Establecer una cantidad por defecto de 1.
        int cantidad = 1;
        
         // --- 3. PARSEO Y VALIDACIÓN DE LA CANTIDAD ---

        try 
        {
            // Solo intentar parsear si cantidadStr no es nulo y no está vacío después de quitar espacios.
            if (cantidadStr != null && !cantidadStr.trim().isEmpty())
            {
                // Convertir la cadena a un entero.
                cantidad = Integer.parseInt(cantidadStr.trim());

                // Asegurar que la cantidad sea al menos 1.
                if (cantidad <= 0) cantidad = 1;
            }
        } 
        catch (NumberFormatException e) 
        {
            // Si la cadena de cantidad no es un número válido, se mantiene la cantidad por defecto (1).
            // No se interrumpe el flujo, se asume 1.
            cantidad = 1;

            System.err.println("AccionAgregarAlCarrito: Cantidad inválida '" + cantidadStr + "', usando 1.");
        }
        
        // --- 4. PARSEO DEL CD SELECCIONADO Y CREACIÓN DEL OBJETO CD ---

        // Solo proceder si se seleccionó un CD y la cadena no está vacía.
        if (cdSeleccionado != null && !cdSeleccionado.isEmpty()) 
        {
            // Usar StringTokenizer para dividir la cadena cdSeleccionado usando '|' como delimitador.
            // Formato esperado: "Titulo|Artista|Pais|$Precio"
            StringTokenizer tokenizer = new StringTokenizer(cdSeleccionado, "|");

            // Verificar si la cadena tiene al menos los 4 componentes esperados.
            if (tokenizer.countTokens() >= 4) 
            {
                String titulo = tokenizer.nextToken().trim(); // Obtener y limpiar el título.
                String artista = tokenizer.nextToken().trim(); // Obtener y limpiar el artista.
                String pais = tokenizer.nextToken().trim(); // Obtener y limpiar el país.
                String precioStr = tokenizer.nextToken().trim().replace("$", "").trim(); // Obtener precio, quitar $ y limpiar.

                try 
                {
                    // Convertir la cadena del precio a float.
                    float precio = Float.parseFloat(precioStr);

                    // Crear una nueva instancia del JavaBean CD utilizando su constructor por defecto.
                    CD nuevoCd = new CD();

                    // Establecer las propiedades del nuevo objeto CD usando sus métodos setter.
                    nuevoCd.setTitulo(titulo);
                    nuevoCd.setArtista(artista);
                    nuevoCd.setPais(pais);
                    nuevoCd.setPrecio(precio);
                    nuevoCd.setCantidad(cantidad); // Establecer la cantidad parseada (o por defecto).
                    
                    
                    // Usar el método agregarItem del objeto Carrito para añadir el nuevoCd.
                    // Este método manejará la lógica de si el CD ya existe (actualizar cantidad)
                    // o si es un nuevo ítem en el carrito.
                    carrito.agregarItem(nuevoCd);
                } 
                catch (NumberFormatException e) 
                {
                    // Si ocurre un error al convertir el precio a float.
                    System.err.println("AccionAgregarAlCarrito: Error al parsear precio: " + precioStr);

                    // Guardar un mensaje de error en el request para que el JSP (si se hiciera forward) lo muestre.
                    // Sin embargo, como esta acción hace sendRedirect, este atributo se perdería
                    // a menos que se pase como parámetro en la URL de redirección o se guarde en sesión.
                    request.setAttribute("errorCarrito", "Error al procesar el CD seleccionado.");
                }
            } 
            else 
            {
                // Si la cadena del CD seleccionado no tiene el formato esperado.
                System.err.println("AccionAgregarAlCarrito: Formato de CD seleccionado incorrecto.");
                request.setAttribute("errorCarrito", "Datos del CD incompletos.");
            }
        }
        
         // --- 5. REDIRECCIÓN ---
        // Esta redirección ayuda a:
        //  a. Evitar que el usuario reenvíe el mismo formulario si recarga la página
        //     después de un POST exitoso (lo que podría añadir el mismo CD de nuevo).
        //  b. Mantener la URL del navegador limpia, mostrando la URL de la vista del carrito.
        response.sendRedirect(request.getContextPath() + "/app?accion=verCarrito"); // Llama al AppController con la acción verCarrito

        // Se devuelve null porque la respuesta ya ha sido manejada por sendRedirect().
        // El AppController no necesita hacer un forward a ningún JSP desde esta acción.
        return null; // Indica que la respuesta ya fue manejada (sendRedirect)
    }
}