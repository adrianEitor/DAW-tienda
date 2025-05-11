<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%-- TÍTULO CAMBIADO A REFLEJAR EL ESTADO --%>
    <title>Caja - Confirmar Pago</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/estilo.css">
    <style>
        .payment-image { width: 80px; height: auto; display: block; margin: 10px auto; }
        .center-text { text-align:center; } /* AÑADIDO PARA CONSISTENCIA */
        .bold-text { font-weight:bold; } /* AÑADIDO */
        .total-amount { font-size:1.2em; color:green; } /* AÑADIDO */
        .error { color: red; } /* AÑADIDO */
        .exito { color: green; } /* AÑADIDO */
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

        <%-- MOSTRAR EL TOTAL A PAGAR --%>
        <%-- Este valor '${importeFinal}' es establecido por AccionCalcularPago
             y también por AccionProcesarPago (para mostrarlo incluso si hay error). --%>
        <p class="bold-text">TOTAL A PAGAR:</p>
        <p class="total-amount">
             <%-- Utiliza la etiqueta JSTL <fmt:formatNumber> para mostrar el importe
                 con formato de moneda (símbolo '$', dos decimales). --%>
            <fmt:formatNumber value="${importeFinal}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2"/>
        </p>

        <%-- FORMULARIO DE CONFIRMACIÓN DE PAGO --%>
        <%-- Este formulario solo se muestra si NO hay un mensaje de éxito (mensajeExito está vacío)
             Y (hay un importe final mayor que cero O hay un mensaje de error previo).
             Esto evita que el usuario intente pagar de nuevo si ya se procesó con éxito
             o si no hay nada que pagar. --%>
        <c:if test="${empty mensajeExito && (importeFinal > 0 || not empty error)}">
            <%-- El action del formulario apunta al AppController. --%>
            <form action="${pageContext.request.contextPath}/app" method="post">
                <%-- Campo oculto accion para indicar al AppController que se está procesando un pago. --%>
                <input type="hidden" name="accion" value="procesarPago">
                
                 <%-- Campo oculto importeFinalConfirmado:
                     Este campo es CRUCIAL. Envía el valor de ${importeFinal} (que se mostró al usuario)
                     de vuelta al servidor (a AccionProcesarPago).
                     AccionProcesarPago puede usar este valor para:
                     1. Confirmar que el importe no ha sido manipulado (comparándolo con el total
                        real del carrito en sesión).
                     2. Registrar el pedido con este importe. --%>
                <input type="hidden" name="importeFinalConfirmado" value="${importeFinal}">
                <br>
                <input type="submit" value="Confirmar y Pagar Ahora">
            </form>
        </c:if>
        
        <%-- Mostrar la imagen de la caja registradora solo si el pago fue exitoso. --%>
        <c:if test="${not empty mensajeExito}">
            <img src="${pageContext.request.contextPath}/images/cash_register.png" alt="Caja" class="payment-image"/>
        </c:if>
        
         <%-- ENLACE PARA VOLVER A LA TIENDA --%>
        <p style="margin-top: 20px;"><a href="${pageContext.request.contextPath}/index.jsp">Volver a la Tienda</a></p>

        <%-- Logica futura de ver mis pedidos. --%>
        <%-- ENLACE OPCIONAL PARA VER PEDIDOS (si el usuario está autenticado y el pago fue exitoso) --%>
        <%-- Comprueba si hay un usuario en sesión y si hubo un mensaje de éxito del pago. --%>

    </div>

    <hr>
</body>
</html>