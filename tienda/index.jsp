<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html> 
  <html>
	<meta charset="UTF-8">
    <head>
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
				<c:when test="${not empty usuario}">
					<p>Bienvenido ${usuario}</p>
				</c:when>
				<c:otherwise>
					<p>Bienvenido desconocido</p>
				</c:otherwise>
			</c:choose>
			<form action="CarritoServlet" method="post">
			<b>CD:</b> 
			<select name="cd">
			<option>Yuan | The Guo Brothers | China | $14.95</option>
			<option>Drums of Passion | Babatunde Olatunji | Nigeria | $16.95</option>
			<option>Kaira | Tounami Diabate| Mali | $16.95</option>
			<option>The Lion is Loose | Eliades Ochoa | Cuba | $13.95</option>
			<option>Dance the Devil Away | Outback | Australia | $14.95</option>
			<option>Record of Changes | Samulnori | Korea | $12.95</option>
			<option>Djelika | Tounami Diabate | Mali | $14.95</option>
			<option>Rapture | Nusrat Fateh Ali Khan | Pakistan | $12.95</option>
			<option>Cesaria Evora | Cesaria Evora | Cape Verde | $16.95</option>
			<option>DAA | GSTIC | Spain | $50.00</option>
			</select>
			<b>Cantidad:</b>
			<input type="text" name="cantidad" value="1">
			<p>
			<center>
				<input type="submit" value="Selecciona Producto">
			</center>
			</form>
		</center>
	  <hr>
    </body>
</html>
