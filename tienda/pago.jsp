<%--
====================================================================================================
FICHERO: pago.jsp
----------------------------------------------------------------------------------------------------
Esta página cumple un doble rol en el flujo de finalización de la compra:

- Confirmación de pago: cuando vengo del carrito después de calcular el total, 
esta página me muestra los productos, el precio final y un botón para "Confirmar y Pagar". 
Si le doy, llamo a la acción procesarPago. Esto pasa si no hay un mensaje de 
pago exitoso todavía.

- Resultado del pago: después de que la acción AccionProcesarPago intente el
pago, vuelvo aquí. La página me dice si el pago fue bien (mensajeExito) 
o si hubo un error. Si todo fue bien, me enseña los productos que compré 
y el total pagado. Esto se muestra si sí hay un mensajeExito.
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

        .boton {
            padding: 10px 15px; /* Ajustado para consistencia con input[type="submit"] */
            border: 1px solid #ccc; 
            background: #5cb85c; 
            color: white; 
            text-decoration: none; 
            border-radius: 8px; 
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            display: inline-block; /* Para que el padding y margin funcionen bien */
            margin-top: 10px;
        }
        .boton:hover {
            background-color: #4cae4c;
        }   
        

        .boton.volver { 
            background: #007bff; 
        }
        .boton.volver:hover {
             background: #0056b3;
        }

        input[type="submit"] { 
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
        
        <%-- ENLACE PARA VOLVER A LA TIENDA (siempre visible) --%>
        <%-- Aplicando tu clase boton y volver a la etiqueta <a> directamente --%>
        <a href="${pageContext.request.contextPath}/index.jsp" class="boton volver" style="margin-top: 20px;">Volver a la Tienda</a>


    </div>

    <hr>
</body>
</html>