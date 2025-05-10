<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

         <p class="bold-text">TOTAL A PAGAR:</p>
        <p class="total-amount">
            <fmt:formatNumber value="${importeFinal}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2"/>
        </p>

        <%-- CAMBIO: El formulario de pago ahora hace POST a AppController con accion=procesarPago --%>
        <%-- Mostrar el botón/formulario de pago solo si no hay mensaje de éxito 
             Y si hay un importe a pagar (o si hubo un error y se quiere reintentar) --%>
        <c:if test="${empty mensajeExito && (importeFinal > 0 || not empty error)}">
            <form action="${pageContext.request.contextPath}/app" method="post">
                <input type="hidden" name="accion" value="procesarPago">
                <%-- Enviar el importe que se está confirmando --%>
                <input type="hidden" name="importeFinalConfirmado" value="${importeFinal}">
                <br>
                <input type="submit" value="Confirmar y Pagar Ahora">
            </form>
        </c:if>
        
        <c:if test="${not empty mensajeExito}">
            <img src="${pageContext.request.contextPath}/images/cash_register.png" alt="Caja" class="payment-image"/>
        </c:if>
        
        <p style="margin-top: 20px;"><a href="${pageContext.request.contextPath}/index.jsp">Volver a la Tienda</a></p>

        <%-- Logica futura de ver mis pedidos. --%>

    </div>

    <hr>
</body>
</html>