<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Caja - Pago Realizado</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/estilo.css">
    <style>
        .payment-image { width: 80px; height: auto; display: block; margin: 10px auto; }
    </style>
</head>
<body>
    <div class="center-text">
        <h1>Caja</h1>
        <p class="bold-text">TOTAL A PAGAR</p>
        <p class="total-amount"><fmt:formatNumber value="${importeFinal}" type="currency" currencySymbol="$"/></p>

        <%-- Imagen como en Figura 3 --%>
        <img src="${pageContext.request.contextPath}/images/cash_register.png" alt="Caja" class="payment-image"/>
        
        <c:choose>
            <c:when test="${not empty usuario}">
                <p><a href="${pageContext.request.contextPath}/PagarServlet?importeFinal=${importeFinal}">Pagar y volver a la página principal</a></p>
            </c:when>
            <c:otherwise>
                <p><a href="${pageContext.request.contextPath}/login.jsp">Registrarse para pagar</a></p>
            </c:otherwise>
        </c:choose>
        <p style="margin-top: 30px;">(Simulación: Gracias por su compra)</p>
    </div>
</body>
</html>