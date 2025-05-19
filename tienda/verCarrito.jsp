<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Carrito de la Compra</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/general.css">
    <style>
        .cart-image { width: 100px; height: auto; display: block; margin: 10px auto; }
        .container { 
            width: 50%; 
            display: flex;
            flex-direction: column;
            align-items: center;
            margin: 30px auto; 
            padding: 25px; 
            border: 1px solid #ccc; 
            background: white; 
            border-radius: 8px; 
            box-shadow: 0 4px 8px rgba(0,0,0,0.1); 
        }
        .centrar-botones{
            width: 50%;
            display: flex;
            flex-direction: row;
            justify-content: space-between;
        }
        .boton {
            padding: 10px;
            flex-direction: column;
            align-items: center;
            border: 1px solid #ccc; 
            background: #5cb85c; 
            border-radius: 8px; 
            box-shadow: 0 4px 8px rgba(0,0,0,0.1); 
        }
        .boton:hover {
            background-color: #4cae4c; 
        }   
        a {
            color: white;
            text-decoration: none;
        }

        .boton:visited {
            color: white;
        }

        .volver {
            background: #007bff; 
        }

        .volver:hover {
            background: #0056b3;
        }
        .eliminar a {
            color: #0056b3;
        }
        
        table {
            width: 100%;
            border-collapse: collapse; /* Bordes unidos */
            font-family: Arial, sans-serif;
        }

        th,
        td {
            border: 1px solid #ddd; /* Gris clarito */
            padding: 10px;
            text-align: left;
        }

        thead {
            background-color: #f5f5f5; /* Cabecera con fondo suave */
        }
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
    <c:choose>

         <%-- Condición <c:when>: Se ejecuta si el carrito NO está vacío.
             EL (${not empty carrito}): Verifica si el atributo carrito (que es una instancia
                                      de tu JavaBean Carrito, puesto en el request por
                                      AccionVerCarrito) no es null.
             EL (${not carrito.vacio}): Accede a la propiedad vacio del JavaBean Carrito
                                      (invocando el método isVacio() o getVacio()).
                                      La negación not verifica que el carrito NO esté vacío. --%>
        <c:when test="${not empty carrito && not carrito.vacio}">
            <div class="container">
                <table class="center-table">
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
                        
                        <%-- 
                            items="${carrito.items}": la colección sobre la que se itera. EL (${carrito.items})
                                                    accede a la propiedad items del JavaBean Carrito
                                                    (invocando su método getItems()), que devuelve la List<CD>.
                            var="cdItem": nombre de la variable que representará cada elemento (un objeto CD)
                                        en cada iteración del bucle.
                            varStatus="loop": crea una variable loop que proporciona información sobre
                                            el estado de la iteración (ej., loop.index para el índice actual). --%>
                        <c:forEach var="cdItem" items="${carrito.items}" varStatus="loop">
                            <tr>

                                <%-- ACCESO A PROPIEDADES DEL JAVABEAN CD (representado por cdItem) USANDO EL --%>
                                <%-- ${cdItem.titulo} invoca cdItem.getTitulo() --%>
                                <%-- ${cdItem.artista} invoca cdItem.getArtista() --%>
                                <%-- ${cdItem.pais} invoca cdItem.getPais() --%>

                                <td>${cdItem.titulo} | ${cdItem.artista} | ${cdItem.pais}</td>
                                <td class="center-text">${cdItem.cantidad}</td> <%-- invoca cdItem.getCantidad() --%>
                                
                                <%-- JSTL <fmt:formatNumber> para mostrar el precio unitario del CD como moneda. --%>
                                <%-- value="${cdItem.precio}" accede a cdItem.getPrecio() --%>
                                <td class="right-text"><fmt:formatNumber value="${cdItem.precio}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2"/></td>
                                
                                <%-- Uso de JSTL <fmt:formatNumber> para mostrar el importe (subtotal) del CD. --%>
                                <%-- value="${cdItem.importe}" accede a la propiedad calculada cdItem.getImporte() --%>
                                <td class="right-text"><fmt:formatNumber value="${cdItem.importe}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2"/></td>
                                
                                <td class="eliminar">
                                    <%-- Enlace para eliminar un ítem. Apunta al AppController con la acción eliminarCD.
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
                <div class="centrar-botones" style="margin-top: 20px;"> 
                    <a href="${pageContext.request.contextPath}/index.jsp"><p class="boton">Sigo comprando</p></a>
                    
                    <a href="${pageContext.request.contextPath}/app?accion=calcularPago"><p class="boton">Pagar</p></a>
                </div>
            </div>

        </c:when>
        <c:otherwise>
            <p>El carrito está vacío.</p>
        </c:otherwise>
    </c:choose>
    <br>
    <div class="boton volver" style="text-align:center;">
        <a href="${pageContext.request.contextPath}/index.jsp">Volver a la Página Principal</a>
    </div>
</body>
</html>