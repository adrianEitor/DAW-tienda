import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.ArrayList;

public class AccionProcesarPago implements Accion 
{
     /**
     * ------------------------------------------------------------------------
     * MÉTODO ejecutar (Implementación de la interfaz Accion)
     * ------------------------------------------------------------------------
     * Contiene la lógica principal para el procesamiento del pago y la creación del pedido.
     */
    @Override
    public String ejecutar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
         System.out.println("AccionProcesarPago: Iniciando procesamiento del pago."); // Log para depuración

        // --- 1. OBTENCIÓN DE OBJETOS DE SESIÓN Y VALIDACIONES INICIALES ---

        HttpSession session = request.getSession(false); // No crear nueva sesión si no existe.
        float importeConfirmado = 0f; // Inicializar el importe que se confirmará.

        List<CD> itemsComprados = null;

        // Si no hay sesión, el usuario no debería estar aquí. Redirigir al login.
        if (session == null) 
        {
            System.out.println("AccionProcesarPago: No hay sesión activa.");

            request.setAttribute("error", "Su sesión ha expirado. Por favor, inicie sesión nuevamente.");
            return "/login.jsp"; // O la ruta configurada a tu login.jsp
        }

        // Obtener el usuario autenticado, el carrito y la instancia de BaseDatos de la sesión.
        Usuario usuarioAutenticado = (Usuario) session.getAttribute("usuarioAutenticado");
        Carrito carrito = (Carrito) session.getAttribute("carrito");
        BaseDatos bd = (BaseDatos) session.getAttribute("bd");

        // Validar que el usuario esté autenticado.
        if (usuarioAutenticado == null) 
        {
            System.out.println("AccionProcesarPago: Usuario no autenticado.");

            request.setAttribute("error", "Debe iniciar sesión para completar el pago.");
            return "/login.jsp";
        }

        // Validar que el carrito exista y no esté vacío.
        if (carrito == null || carrito.isVacio()) 
        {
            System.out.println("AccionProcesarPago: Carrito nulo o vacío.");
            request.setAttribute("mensaje", "Su carrito está vacío. No se puede procesar el pago.");

            // Si el carrito es null, se crea uno vacío para que el JSP no falle al intentar acceder a sus propiedades.
            request.setAttribute("carrito", carrito != null ? carrito : new Carrito());

            return "/verCarrito.jsp"; // O a index
        }

        // Validar que la instancia de BaseDatos esté disponible en sesión.
        if (bd == null) 
        {
            System.err.println("AccionProcesarPago: BaseDatos no encontrada en sesión.");

            request.setAttribute("error", "Error interno del sistema (BD no disponible).");
            
            // Intentar pasar el importe actual del carrito para mostrarlo en la página de pago con el error.
            importeConfirmado = carrito.getTotalGeneral();

            request.setAttribute("importeFinal", importeConfirmado);
            
            return "/pago.jsp";
        }

        // --- 2. OBTENCIÓN Y VALIDACIÓN DEL IMPORTE CONFIRMADO ---
        // Se espera que pago.jsp envíe el importe final como un parámetro llamado "importeFinalConfirmado".
        // Esto es una medida de confirmación, aunque el total real se recalculará o se usará el del carrito.
        String importeFinalStr = request.getParameter("importeFinalConfirmado");
        
        try 
        {
            if (importeFinalStr != null) 
            {
                importeConfirmado = Float.parseFloat(importeFinalStr);

                System.out.println("AccionProcesarPago: Importe confirmado recibido: " + importeConfirmado);

                // Validación CRUCIAL: Comparar el importe recibido con el total real del carrito en sesión.
                // Esto evita que un usuario malintencionado modifique el importe en el formulario.
                // Se usa una pequeña tolerancia para comparaciones de tipo float.
                if (Math.abs(importeConfirmado - carrito.getTotalGeneral()) > 0.01) 
                {
                    System.err.println("AccionProcesarPago: ¡ALERTA DE SEGURIDAD! Discrepancia de importe. Recibido: " +
                                       importeConfirmado + ", Calculado del carrito: " + carrito.getTotalGeneral());

                    throw new NumberFormatException("Discrepancia de importe. La operación no puede continuar.");
                }
            } 
            else 
            {
                System.err.println("AccionProcesarPago: Parámetro 'importeFinalConfirmado' no recibido o vacío.");

                throw new NumberFormatException("Importe final no proporcionado por el cliente.");
            }

             // --- 3. CREACIÓN Y PERSISTENCIA DEL PEDIDO ---
            // Crear un nuevo objeto Pedido.
            // Se usa el constructor de Pedido que toma (usuarioId, importeTotal)
            // y luego se establece la fecha si Pedido es un JavaBean con setFechaPedido.
            Pedido nuevoPedido = new Pedido(usuarioAutenticado.getId(), importeConfirmado);
            nuevoPedido.setFechaPedido(new Timestamp(System.currentTimeMillis()));
            
            System.out.println("AccionProcesarPago: Intentando agregar pedido a la BD.");
            bd.agregarPedido(nuevoPedido); // Llamar al método de BaseDatos que usa PedidosDAO.
            System.out.println("AccionProcesarPago: Pedido agregado a la BD con éxito.");

            // === CAMBIO PARA GUARDAR ITEMS DEL PEDIDO PARA EL INFORME ===
            if (carrito.getItems() != null) 
            {
                // Crear una nueva lista que contenga los mismos objetos CD (copia superficial)
                itemsComprados = new ArrayList<>(carrito.getItems());
                System.out.println("AccionProcesarPago: Items del pedido copiados para el resumen: " + itemsComprados.size() + " items.");
            }
            // === FIN CAMBIO PARA GUARDAR ITEMS DEL PEDIDO PARA EL INFORME ===
            
              
            // --- 4. LIMPIEZA DEL CARRITO ---

            // Después de que el pedido se ha guardado exitosamente en la BD.

            carrito.vaciarCarrito(); // Llamar al método del JavaBean Carrito.

            // Volver a guardar el carrito (ahora vacío) en la sesión.
            session.setAttribute("carrito", carrito); 
            System.out.println("AccionProcesarPago: Carrito vaciado de la sesión.");

            // Establecer un mensaje de éxito para mostrar en pago.jsp.
            request.setAttribute("mensajeExito", "¡Gracias! Su pedido ha sido procesado con éxito.");

            // === PASAR ITEMS COMPRADOS AL JSP ===
            if (itemsComprados != null)
            {
                request.setAttribute("itemsDelPedido", itemsComprados);
            }
            // === FIN PARA PASAR ITEMS COMPRADOS AL JSP ===

        } 
        catch (NumberFormatException e) 
        {
            // Error si el importeFinalStr no es un float válido o si hay discrepancia.
            System.err.println("AccionProcesarPago: NumberFormatException - " + e.getMessage());
            e.printStackTrace();

            request.setAttribute("error", "Error con el importe del pago: " + e.getMessage());

            // Si hubo error al parsear, es posible que importeConfirmado siga siendo 0.
            // Es mejor mostrar el total real del carrito si está disponible.
            if (carrito != null) importeConfirmado = carrito.getTotalGeneral();
        } 
        catch (SQLException e) 
        {
            // Error si falla la inserción en la base de datos.
            System.err.println("AccionProcesarPago: SQLException - " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error de base de datos al guardar su pedido: " + e.getMessage());

            // El importeConfirmado (si se parseó bien) se mantiene para mostrarlo.
            // Si el parseo falló antes, importeConfirmado podría ser 0.
            if (carrito != null && importeConfirmado == 0f) importeConfirmado = carrito.getTotalGeneral();
        }
        
        // --- 5. PREPARAR DATOS PARA LA VISTA FINAL Y DEVOLVER RUTA ---

        // Siempre pasar el importeConfirmado (o el calculado del carrito si hubo error antes)
        // al JSP para que se muestre, incluso si hubo un error.
        request.setAttribute("importeFinal", importeConfirmado); // Siempre pasar el importe

         System.out.println("AccionProcesarPago: Haciendo forward a /pago.jsp");
         
        // Devolver la ruta a pago.jsp. Esta página mostrará el mensaje de éxito o error.
        return "/pago.jsp"; // Siempre ir a pago.jsp para mostrar resultado
    }
}