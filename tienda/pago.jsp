<%--
====================================================================================================
ARCHIVO: pago.jsp
----------------------------------------------------------------------------------------------------
PROPÓSITO DE ESTA PÁGINA JSP:
Esta página cumple un doble rol en el flujo de finalización de la compra:

1.  ESTADO DE CONFIRMACIÓN DE PAGO:

    - Se muestra cuando el usuario es redirigido aquí DESPUÉS de que la acción
      AccionCalcularPago (invocada desde el carrito) ha preparado el total a pagar.

    - En este estado, la página:

        a) Muestra un resumen de los ítems que están actualmente en el carrito 
           (obtenidos del atributo carritoParaConfirmar puesto en el request por AccionCalcularPago).

        b) Muestra el importeFinal calculado.

        c) Presenta un formulario con un botón "Confirmar y Pagar Ahora".

    - Al enviar este formulario, se invoca la acción procesarPago del AppController.

    - Este estado se activa visualmente cuando el atributo mensajeExito (que indica
    un pago ya procesado) está vacío o no existe en el request.

2.  ESTADO DE RESULTADO DEL PAGO:

    - Se muestra cuando el usuario es redirigido aquí DESPUÉS de que la acción
      AccionProcesarPago ha intentado procesar el pago y guardar el pedido.

    - En este estado, la página:

        a) Muestra un mensaje de éxito (mensajeExito) o de error (error)
           proveniente de AccionProcesarPago.

        b) Si el pago fue exitoso, muestra un informe detallado de los ítems que se
           compraron (obtenidos del atributo itemsDelPedido puesto en el request
           por AccionProcesarPago, que es una copia de los ítems del carrito antes de vaciarlo).

        c) Muestra el importeFinal que fue procesado (o intentado procesar).

    - Este estado se activa visualmente cuando el atributo mensajeExito está presente
      en el request.

FLUJO DE DATOS ESPERADO:
- DESDE AccionCalcularPago (para el estado de confirmación):
    - request.setAttribute("importeFinal", floatConElTotal);
    - request.setAttribute("carritoParaConfirmar", objetoCarritoActual);
    
- DESDE AccionProcesarPago (para el estado de resultado):
    - request.setAttribute("mensajeExito", "Mensaje de éxito..." ); O request.setAttribute("error", "Mensaje de error...");
    - request.setAttribute("itemsDelPedido", List<CD>ConItemsComprados); (si éxito)
    - request.setAttribute("importeFinal", floatConElTotalProcesado);

La lógica condicional dentro de este JSP (usando JSTL <c:if> y <c:choose>)
determina qué secciones se renderizan basándose en la presencia y el valor de
estos atributos del request.
====================================================================================================
--%>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    
    <title>Caja - Proceso de pago</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/general.css">

     <style>
        /* ESTILOS BÁSICOS (puedes moverlos a tu estilo.css) */
        body { font-family: Arial, sans-serif; background-color: #FDF5E6; margin-top: 20px;}
        .container { width: 70%; max-width: 800px; margin: 30px auto; padding: 20px; background-color: white; border: 1px solid #ddd; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        .center-text { text-align:center; }
        .bold-text { font-weight:bold; }
        .total-amount { font-size:1.2em; color:green; margin-bottom: 20px;}
        .error { color: #D8000C; background-color: #FFD2D2; padding:10px; border-radius:4px; margin-bottom: 15px; text-align:center; }
        .exito { color: #4F8A10; background-color: #DFF2BF; padding:10px; border-radius:4px; margin-bottom: 15px; text-align:center; }
        .payment-image { width: 80px; height: auto; display: block; margin: 20px auto; }
        
        .tabla-resumen { /* Para ambos resúmenes: antes y después de pagar */
            width: 90%; 
            margin: 20px auto; 
            border-collapse: collapse; 
            font-size: 0.9em;
        }
        .tabla-resumen th, .tabla-resumen td { 
            border: 1px solid #e0e0e0; 
            padding: 8px; 
            text-align: left; 
        }
        .tabla-resumen th { 
            background-color: #f7f7f7; 
            font-weight: bold;
        }

        .tabla-resumen td {
    background-color: #fff; 
        }

        .tabla-resumen .right-text { text-align: right; }
        .tabla-resumen .center-text { text-align: center; }

        /* TUS ESTILOS ORIGINALES PARA BOTONES Y ENLACES */
        .boton {
            padding: 10px 15px; /* Ajustado para consistencia con input[type="submit"] */
            /* flex-direction, align-items no aplican bien a <a> o <p> directamente */
            border: 1px solid #ccc; 
            background: #5cb85c; 
            color: white; /* AÑADIDO: para que el texto del enlace sea blanco */
            text-decoration: none; /* AÑADIDO: para quitar subrayado de enlace */
            border-radius: 8px; 
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            display: inline-block; /* Para que el padding y margin funcionen bien */
            margin-top: 10px;
        }
        .boton:hover {
            background-color: #4cae4c;
        }   
        /* 'a' global no es ideal, mejor aplicar clases a los enlaces específicos */
        /* a { color: white; text-decoration: none; } */

        .boton.volver { /* Clase específica para el botón/enlace "Volver" */
            background: #007bff; 
        }
        .boton.volver:hover {
             background: #0056b3;
        }

        input[type="submit"] { /* Estilo para el botón de submit */
            padding: 10px 15px;
            border: 1px solid #4cae4c; 
            background: #5cb85c; 
            color: white;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            cursor: pointer;
            font-size: 1em;
        }
        input[type="submit"]:hover {
            background-color: #4cae4c;
        }
        
    </style>

</head>

<body>
    <div class="center-text">
        <h1>Caja</h1>
        <hr>

        <%-- MOSTRAR MENSAJES DE ÉXITO O ERROR DEL AppController (via AccionProcesarPago) --%>
        <c:if test="${not empty mensajeExito}">
            <h2 class="exito">${mensajeExito}</h2>
        </c:if>

        <c:if test="${not empty error}">
            <h2 class="error">Error: ${error}</h2>
        </c:if>


        <%-- ======================================================================= --%>
        <%-- SECCIÓN DE CONFIRMACIÓN (SE MUESTRA SI NO HAY MENSAJE DE ÉXITO)       --%>
        <%-- Y si hay un importeFinal (establecido por AccionCalcularPago)         --%>
        <%-- ======================================================================= --%>
        <c:if test="${empty mensajeExito && not empty importeFinal}">
            <h3>Por favor, confirme su pedido:</h3>

            <%-- Mostrar resumen del carrito ANTES de pagar --%>
            <%-- carritoParaConfirmar es el atributo establecido por AccionCalcularPago --%>
            <c:if test="${not empty carritoParaConfirmar && not carritoParaConfirmar.vacio}">
                <table class="tabla-resumen">
                    <thead>
                        <tr>
                            <th>Producto</th>
                            <th class="center-text">Cantidad</th>
                            <th class="right-text">Precio Unit.</th>
                            <th class="right-text">Subtotal</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${carritoParaConfirmar.items}">
                            <tr>
                                <td>${item.titulo} | ${item.artista}</td>
                                <td class="center-text">${item.cantidad}</td>
                                <td class="right-text"><fmt:formatNumber value="${item.precio}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2"/></td>
                                <td class="right-text"><fmt:formatNumber value="${item.importe}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2"/></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <br>
            </c:if>


            <p class="bold-text">TOTAL A PAGAR:</p>
            <p class="total-amount">
                <fmt:formatNumber value="${importeFinal}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2"/>
            </p>

            <%-- Formulario de confirmación de pago --%>
            <%-- Se muestra si no hubo un error crítico que impida pagar (error de BD, etc., establecido por AccionCalcularPago)
                 Y si hay un importe a pagar. --%>
            <c:if test="${empty error && importeFinal > 0}">
                <form action="${pageContext.request.contextPath}/app" method="post">
                    <input type="hidden" name="accion" value="procesarPago">
                    <input type="hidden" name="importeFinalConfirmado" value="${importeFinal}">
                    <br>
                    <input type="submit" value="Confirmar y Pagar Ahora">
                </form>
            </c:if>
        </c:if>

        <%-- ======================================================================= --%>
        <%-- SECCIÓN DE RESULTADO DEL PAGO (SE MUESTRA SI HAY MENSAJE DE ÉXITO)      --%>
        <%-- ======================================================================= --%>
        <c:if test="${not empty mensajeExito}">
            <%-- Mostrar resumen del pedido DESPUÉS de pagar --%>
            <%-- itemsDelPedido es el atributo establecido por AccionProcesarPago --%>
            <c:if test="${not empty itemsDelPedido}">
                <h3>Detalle de su compra Realizada:</h3>
                <table class="tabla-resumen">
                     <thead>
                        <tr>
                            <th>Producto</th>
                            <th class="center-text">Cantidad</th>
                            <th class="right-text">Precio Unit.</th>
                            <th class="right-text">Subtotal</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="itemComprado" items="${itemsDelPedido}">
                            <tr>
                                <td>${itemComprado.titulo} | ${itemComprado.artista}</td>
                                <td class="center-text">${itemComprado.cantidad}</td>
                                <td class="right-text">
                                    <fmt:formatNumber value="${itemComprado.precio}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2"/>
                                </td>
                                <td class="right-text">
                                    <fmt:formatNumber value="${itemComprado.importe}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2"/>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <br>
            </c:if>
            
            <%-- El total pagado se muestra al principio junto con el mensaje de éxito/error --%>
            <%-- Si quieres repetirlo aquí:
            <p class="bold-text">TOTAL PAGADO:</p>
            <p class="total-amount">
                <fmt:formatNumber value="${importeFinal}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2"/>
            </p>
            --%>
            <img src="${pageContext.request.contextPath}/images/cash_register.png" alt="Caja Registradora" class="payment-image"/>
        </c:if>
        
        <%-- ENLACE PARA VOLVER A LA TIENDA (Siempre visible) --%>
        <%-- Aplicando tu clase boton y volver a la etiqueta <a> directamente --%>
        <a href="${pageContext.request.contextPath}/index.jsp" class="boton volver" style="margin-top: 20px;">Volver a la Tienda</a>


    </div>

    <hr>
</body>
</html>