<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Carrito de la Compra</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/estilo.css">
    <%-- Imagen del carrito para los enlaces, como en Figura 2 --%>
    <style>
        .cart-image { width: 100px; height: auto; display: block; margin: 10px auto; }
        .payment-image { width: 80px; height: auto; display: block; margin: 10px auto; } /* Para Figura 3 */
    </style>
</head>
<body>
    <h1 class="center-text">Carrito de la compra</h1>

    <c:choose>
        <c:when test="${not empty carrito}">
            <table class="center-table" border="1">
                <thead>
                    <tr>
                        <th>TÍTULO DEL CD</th>
                        <th>Cantidad</th>
                        <th>Importe</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="cd" items="${carrito}" varStatus="loop">
                        <tr>
                            <td>${cd.titulo} | ${cd.artista} | ${cd.pais} | <fmt:formatNumber value="${cd.precio}" type="currency" currencySymbol="$"/></td>
                            <td class="center-text">${cd.cantidad}</td>
                            <td class="right-text"><fmt:formatNumber value="${cd.importe}" type="currency" currencySymbol="$"/></td>
                            <td class="center-text">
                                <a href="${pageContext.request.contextPath}/EliminarCDServlet?index=${loop.index}">Eliminar uno</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="2" class="bold-text">IMPORTE TOTAL</td>
                        <td class="right-text bold-text"><fmt:formatNumber value="${totalGeneral}" type="currency" currencySymbol="$"/></td>
                        <td></td>
                    </tr>
                </tfoot>
            </table>

            <%-- Imágenes y enlaces como en Figura 2 --%>
            <div class="center-text" style="margin-top: 20px;">
                <img src="${pageContext.request.contextPath}/images/shopping_cart.png" alt="Carrito" class="cart-image"/>
                <a href="${pageContext.request.contextPath}/index.jsp">Sigo comprando</a> |
                <a href="${pageContext.request.contextPath}/CalcularPagoServlet">Me largo a pagar</a>
            </div>

        </c:when>
        <c:otherwise>
            <p class="center-text">El carrito está vacío.</p>
            <p class="center-text"><a href="${pageContext.request.contextPath}/index.jsp">Volver a la tienda</a></p>
        </c:otherwise>
    </c:choose>
</body>
</html>