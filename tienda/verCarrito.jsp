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
        /* .payment-image { width: 80px; height: auto; display: block; margin: 10px auto; } no se usa aquí */
    </style>
</head>
<body>
    <h1 class="center-text">Carrito de la compra</h1>
    <div align="center">
        <%-- MOSTRAR ERRORES O MENSAJES DEL CARRITO SI LOS HAY --%>
        <c:if test="${not empty errorCarrito}">
            <p style="color:red;">${errorCarrito}</p>
        </c:if>
        <c:if test="${not empty sessionScope.usuarioAutenticado}">
            <p>Usuario: ${sessionScope.usuarioAutenticado.nombre}</p>
        </c:if>
    </div>

    <%-- LÓGICA DE PRESENTACIÓN DEL CARRITO USANDO JSTL y EL --%>
    <%-- JSTL <c:choose> permite una estructura condicional similar a if-else if-else. --%>
    <c:choose>

         <%-- Condición <c:when>: Se ejecuta si el carrito NO está vacío.
             EL (${not empty carrito}): Verifica si el atributo carrito (que es una instancia
                                      de tu JavaBean Carrito, puesto en el request por
                                      AccionVerCarrito) no es null.
             EL (${not carrito.vacio}): Accede a la propiedad vacio del JavaBean Carrito
                                      (invocando el método isVacio() o getVacio()).
                                      La negación not verifica que el carrito NO esté vacío. --%>
        <c:when test="${not empty carrito && not carrito.vacio}">
            <table class="center-table" border="1">
                <thead>
                    <tr>
                        <th>TÍTULO DEL CD</th>
                        <th>Cantidad</th>
                        <th>Precio Unit.</th> <%-- TÍTULO DE COLUMNA MÁS CLARO --%>
                        <th>Subtotal</th>    <%-- TÍTULO DE COLUMNA MÁS CLARO --%>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>
                    
                    <%-- JSTL <c:forEach>: Itera sobre una colección.
                         items="${carrito.items}": La colección sobre la que se itera. EL (${carrito.items})
                                                  accede a la propiedad 'items' del JavaBean 'Carrito'
                                                  (invocando su método getItems()), que devuelve la List<CD>.
                         var="cdItem": Nombre de la variable que representará cada elemento (un objeto CD)
                                       en cada iteración del bucle.
                         varStatus="loop": Crea una variable 'loop' que proporciona información sobre
                                           el estado de la iteración (ej., loop.index para el índice actual). --%>
                    <c:forEach var="cdItem" items="${carrito.items}" varStatus="loop">
                        <tr>

                            <%-- ACCESO A PROPIEDADES DEL JAVABEAN CD (representado por cdItem) USANDO EL --%>
                            <%-- ${cdItem.titulo} invoca cdItem.getTitulo() --%>
                            <%-- ${cdItem.artista} invoca cdItem.getArtista() --%>
                            <%-- ${cdItem.pais} invoca cdItem.getPais() --%>

                            <td>${cdItem.titulo} | ${cdItem.artista} | ${cdItem.pais}</td>
                            <td class="center-text">${cdItem.cantidad}</td> <%-- invoca cdItem.getCantidad() --%>
                            
                            <%-- Uso de JSTL <fmt:formatNumber> para mostrar el precio unitario del CD como moneda. --%>
                            <%-- value="${cdItem.precio}" accede a cdItem.getPrecio() --%>
                            <td class="right-text"><fmt:formatNumber value="${cdItem.precio}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2"/></td>
                            
                            <%-- Uso de JSTL <fmt:formatNumber> para mostrar el importe (subtotal) del CD. --%>
                            <%-- value="${cdItem.importe}" accede a la propiedad calculada cdItem.getImporte() --%>
                            <td class="right-text"><fmt:formatNumber value="${cdItem.importe}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2"/></td>
                            
                            <td class="center-text">
                                <%-- Enlace para eliminar un ítem. Apunta al AppController con la acción 'eliminarCD'.
                                     Se pasa el index del ítem actual en el bucle como parámetro de la URL.
                                     EL (${loop.index}) obtiene el índice de la iteración actual. --%>
                                <a href="${pageContext.request.contextPath}/app?accion=eliminarCD&index=${loop.index}">Eliminar uno</a>
                            </td>

                        </tr>
                    </c:forEach>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="3" class="bold-text right-text">IMPORTE TOTAL:</td>
                        <%-- ACCEDE AL TOTAL A TRAVÉS DE ${carrito.totalGeneral} --%>
                        <td class="right-text bold-text"><fmt:formatNumber value="${carrito.totalGeneral}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2"/></td>
                        <td></td> 
                    </tr>
                </tfoot>
            </table>

            <div class="center-text" style="margin-top: 20px;">
                <img src="${pageContext.request.contextPath}/images/shopping_cart.png" alt="Carrito" class="cart-image"/>
                <a href="${pageContext.request.contextPath}/index.jsp">Sigo comprando</a> |
                <%-- CAMBIO: ENLACE A AppController CON ACCIÓN --%>
                <a href="${pageContext.request.contextPath}/app?accion=calcularPago">Me largo a pagar</a>
            </div>

        </c:when>
        <c:otherwise>
            <p class="center-text">El carrito está vacío.</p>
            <p class="center-text"><a href="${pageContext.request.contextPath}/index.jsp">Volver a la tienda</a></p>
        </c:otherwise>
    </c:choose>
    <br>
    <div style="text-align:center;">
        <a href="${pageContext.request.contextPath}/index.jsp">Volver a la Página Principal</a>
    </div>
</body>
</html>