<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Carrito de la Compra</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/estilo.css">
    <style>
        .cart-image { width: 100px; height: auto; display: block; margin: 10px auto; }
    </style>
</head>
<body>
    <h1 class="center-text">Carrito de la compra</h1>

    <c:choose>
        <c:when test="${not empty carrito && not carrito.vacio}">
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
                    <%-- Accede a la lista de items a través de carrito.getItems() --%>
                    <c:forEach var="cdItem" items="${carrito.items}" varStatus="loop">
                        <tr>
                            <td>${cdItem.titulo} | ${cdItem.artista} | ${cdItem.pais}</td>
                            <td class="center-text">${cdItem.cantidad}</td>
                            <td class="right-text"><fmt:formatNumber value="${cdItem.precio}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2"/></td>
                            <td class="right-text"><fmt:formatNumber value="${cdItem.importe}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2"/></td>
                            <td class="center-text">
                                <a href="${pageContext.request.contextPath}/EliminarCDServlet?index=${loop.index}">Eliminar uno</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="3" class="bold-text right-text">IMPORTE TOTAL:</td>
                        <%-- Accede al total a través de carrito.getTotalGeneral() --%>
                        <td class="right-text bold-text"><fmt:formatNumber value="${carrito.totalGeneral}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2"/></td>
                        <td></td> 
                    </tr>
                </tfoot>
            </table>

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