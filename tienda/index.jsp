<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html> 
  <html>
    <head>
	  <meta charset="UTF-8">
      <title>Música para DAA</title>
    </head>
    <body bgcolor="#FDF5E6">
      <table align="center" border="0">
		<tr> 
		<th><IMG SRC="" ALIGN="CENTER"></th>
		<th><font face="Times New Roman,Times" size="+3">Música para DAA</font></th>
		<th><IMG SRC="" ALIGN="CENTER"></th>
		</tr>
      </table>
      <hr>
		<p>
		<center>
			<c:choose>
				<c:when test="${not empty sessionScope.usuarioAutenticado}">
					<p>Bienvenido, ${sessionScope.usuarioAutenticado.nombre}!</p>
					<%-- <a href="${pageContext.request.contextPath}/app?accion=logout">Cerrar Sesión</a> --%>
				</c:when>

				<c:otherwise>
					<p>Bienvenido desconocido</p>

					<%-- ENLACES APUNTAN A JSP DIRECTAMENTE --%>
                	<a href="${pageContext.request.contextPath}/login.jsp">Iniciar Sesión</a> | 
                	<a href="${pageContext.request.contextPath}/login.jsp">Registrarse</a>

				</c:otherwise>
			</c:choose>

			<form action="${pageContext.request.contextPath}/app" method="post">
				<input type="hidden" name="accion" value="agregarCD">
				<b>CD:</b> 
			<select name="cd">

			    <option value="Yuan|The Guo Brothers|China|$14.95">Yuan | The Guo Brothers | China | $14.95</option>
                <option value="Drums of Passion|Babatunde Olatunji|Nigeria|$16.95">Drums of Passion | Babatunde Olatunji | Nigeria | $16.95</option>
                <option value="Kaira|Tounami Diabate|Mali|$16.95">Kaira | Tounami Diabate| Mali | $16.95</option>
                <option value="The Lion is Loose|Eliades Ochoa|Cuba|$13.95">The Lion is Loose | Eliades Ochoa | Cuba | $13.95</option>
                <option value="Dance the Devil Away|Outback|Australia|$14.95">Dance the Devil Away | Outback | Australia | $14.95</option>
                <option value="Record of Changes|Samulnori|Korea|$12.95">Record of Changes | Samulnori | Korea | $12.95</option>
                <option value="Djelika|Tounami Diabate|Mali|$14.95">Djelika | Tounami Diabate | Mali | $14.95</option>
                <option value="Rapture|Nusrat Fateh Ali Khan|Pakistan|$12.95">Rapture | Nusrat Fateh Ali Khan | Pakistan | $12.95</option>
                <option value="Cesaria Evora|Cesaria Evora|Cape Verde|$16.95">Cesaria Evora | Cesaria Evora | Cape Verde | $16.95</option>
                <option value="DAA|GSTIC|Spain|$50.00">DAA | GSTIC | Spain | $50.00</option>

			</select>

			<b>Cantidad:</b>

			<input type="number" name="cantidad" value="1" min="1">
			<p>
			<center>
				<input type="submit" value="Añadir al Carrito">
			</center>
			</form>

		<%-- CAMBIO: ENLACE A AppController --%>
        <p><a href="${pageContext.request.contextPath}/app?accion=verCarrito">Ver Carrito</a></p>
		
		<%-- POSIBLE MEJORA: Lógica para "Ver Mis Pedidos" si el usuario está autenticado --%>

		</center>
	  <hr>
    </body>
</html>
