<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Iniciar Sesión o Registrarse</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #FDF5E6; }
        .container { width: 300px; margin: 50px auto; padding: 20px; border: 1px solid #ccc; background: white; }
        .form-group { margin-bottom: 15px; }
        input[type="text"], input[type="password"] { width: 100%; padding: 8px; }
        .btn { background: #4CAF50; color: white; padding: 10px; border: none; cursor: pointer; }
        .error { color: red; }
    </style>
</head>
<body>
    <div class="container">
        <h2>Iniciar Sesión</h2>
        <%-- Mensaje de error --%>
        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>

        <%-- Mensaje de exito --%>
        <c:if test="${not empty exito}">
            <p style="color: green;">${exito}</p>
        </c:if>

        <form action="LoginServlet" method="post">
            <div class="form-group">
                <label>Email:</label>
                <input type="text" name="email" required>
            </div>
            <div class="form-group">
                <label>Contraseña:</label>
                <input type="password" name="password" required>
            </div>
            <input type="submit" value="Iniciar Sesión" class="btn">
        </form>

        <hr>
        <h3>¿No tienes cuenta?</h3>
        <form action="RegistroServlet" method="post">
            <div class="form-group">
                <label>Nombre:</label>
                <input type="text" name="nombre" required>
            </div>
            <div class="form-group">
                <label>Email:</label>
                <input type="text" name="email" required>
            </div>
            <div class="form-group">
                <label>Contraseña:</label>
                <input type="password" name="password" required>
            </div>
            <div class="form-group">
                <label>Tarjeta de crédito:</label>
                <input type="text" name="tarjeta" required>
            </div>
            <input type="submit" value="Registrarse" class="btn">
        </form>
    </div>
</body>
</html>