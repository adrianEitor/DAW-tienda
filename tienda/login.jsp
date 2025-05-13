<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %> <%-- Buena práctica tenerla, aunque sea el default --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Iniciar Sesión o Registrarse</title>
    <%-- ENLACE A TU CSS EXTERNO SI LO TIENES --%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/general.css">
    <style>
        .container { 
            width: 350px; /* Un poco más de ancho */
            margin: 30px auto; 
            padding: 25px; 
            border: 1px solid #ccc; 
            background: white; 
            border-radius: 8px; /* Bordes más redondeados */
            box-shadow: 0 4px 8px rgba(0,0,0,0.1); /* Sombra más pronunciada */
        }
        .form-group { 
            margin-bottom: 18px; /* Más espacio entre grupos */
        }
        .form-group label { 
            display: block; 
            margin-bottom: 6px; /* Espacio entre label e input */
            font-weight: bold; 
            color: #555; /* Color de label */
        }
        /* ESTILO COMÚN PARA INPUTS DE TEXTO, EMAIL, PASSWORD */
        input[type="text"], 
        input[type="password"], 
        input[type="email"] { 
            width: 100%; 
            padding: 10px; /* Padding más generoso */
            border: 1px solid #ddd; 
            border-radius: 4px; 
            box-sizing: border-box; /* IMPORTANTE para que el padding no aumente el ancho total */
            font-size: 1em;
        }
        .btn { 
            color: white; 
            padding: 12px 18px; /* Botones más grandes */
            border: none; 
            cursor: pointer; 
            border-radius: 4px; 
            font-size: 1.05em; /* Fuente ligeramente más grande */
            width: 100%; /* Para que ocupen todo el ancho disponible */
            margin-top: 10px; /* Espacio antes del botón */
            transition: background-color 0.3s ease; /* Transición suave */
        }
        .btn-login { background: #5cb85c; /* Verde para login */ }
        .btn-login:hover { background: #4cae4c; }
        .btn-register { background: #007bff; /* Azul para registro */ }
        .btn-register:hover { background: #0056b3; }

        .error { color: #D8000C; background-color: #FFD2D2; padding:10px; border-radius:4px; margin-bottom: 15px; text-align:center; }
        .exito { color: #4F8A10; background-color: #DFF2BF; padding:10px; border-radius:4px; margin-bottom: 15px; text-align:center; }
        
        hr { margin-top: 25px; margin-bottom: 25px; border: 0; border-top: 1px solid #eee; }
        .link-container { text-align:center; margin-top:20px; }
        .link-container a { color: #007bff; text-decoration:none; }
        .link-container a:hover { text-decoration:underline; }
    </style>
</head>
<body>
    <div class="container">
        <h2>Iniciar Sesión</h2>
        
        <%-- Mensaje de error específico para login --%>
        <c:if test="${not empty errorLogin}">
            <p class="error">${errorLogin}</p>
        </c:if>
        <%-- Mensaje de éxito específico para registro (se muestra en la página de login) --%>
        <c:if test="${not empty exitoRegistro}">
            <p class="exito">${exitoRegistro}</p>
        </c:if>

        <%-- ======================================================================= --%>
        <%-- FORMULARIO DE INICIO DE SESIÓN                                            --%>
        <%-- ======================================================================= --%>
        <%-- action: URL a la que se enviarán los datos del formulario.
             Aquí, apunta al AppController (${pageContext.request.contextPath}/app).
             El AppController usará el parámetro accion para determinar qué hacer. --%>
        <%-- method="post": Los datos del formulario se enviarán en el cuerpo de la petición HTTP,
             lo cual es más seguro para credenciales que enviarlos en la URL (como haría GET). --%>
        <form action="${pageContext.request.contextPath}/app" method="post">
            
            <%-- CAMPO OCULTO accion:
                 Este campo no es visible para el usuario, pero su valor ("login") se envía
                 junto con los otros datos del formulario. El AppController leerá este
                 parámetro para saber que esta petición específica es para la acción de login. --%>
            <input type="hidden" name="accion" value="login">

            <div class="form-group">
                <label for="emailLogin">Email:</label>
                <%-- CAMPO DE ENTRADA PARA EL EMAIL:
                     type="email": Indica al navegador que espere una dirección de email,
                                   puede activar validaciones básicas del navegador.
                     id="emailLogin": Identificador único para este elemento en la página (útil para JavaScript o el 'for' de la label).
                     name="email_login": NOMBRE DEL PARÁMETRO que se enviará al servidor.
                                        AccionLogin leerá request.getParameter("email_login").
                     required: Atributo HTML5 que indica que este campo no puede estar vacío.
                     value="${param.email_login}": REPOBLACIÓN DEL CAMPO. Si AccionLogin detecta un error
                                                 y hace forward de vuelta a esta página, y si puso
                                                 el email original como un atributo del request llamado
                                                 "param_email_login" (o si el forward conserva los parámetros originales),
                                                 este campo se rellenará con el valor que el usuario había ingresado.
                                                 Alternativamente, si AccionLogin usa request.setAttribute("emailLoginValor", email);
                                                 entonces aquí sería value="${emailLoginValor}".
                                                 `${param.nombre}` accede directamente a los parámetros de la petición original. --%>
                <input type="email" id="emailLogin" name="email_login" required value="${param.email_login}">
            </div>

            <div class="form-group">
                <label for="passwordLogin">Contraseña:</label>
                 <%-- CAMPO DE ENTRADA PARA LA CONTRASEÑA:
                     type="password": Los caracteres se muestran ocultos (ej. asteriscos).
                     id="passwordLogin": Identificador único.
                     name="password_login": NOMBRE DEL PARÁMETRO para el servidor.
                     required: Campo obligatorio.
                     (No se repobla la contraseña por razones de seguridad). --%>
                <input type="password" id="passwordLogin" name="password_login" required>
            </div>
            
            <%-- BOTÓN DE ENVÍO DEL FORMULARIO DE LOGIN --%>
            <input type="submit" value="Iniciar Sesión" class="btn btn-login">

        </form>

        <hr>
        <h3>¿No tienes cuenta?</h3>
        
        <%-- Mensaje de error específico para registro --%>
        <c:if test="${not empty errorRegistro}">
            <p class="error">${errorRegistro}</p>
        </c:if>

        <%-- ======================================================================= --%>
        <%-- FORMULARIO DE REGISTRO                                                    --%>
        <%-- ======================================================================= --%>
        <%-- 'action': También apunta al AppController. --%>
        <form action="${pageContext.request.contextPath}/app" method="post">
            <%-- CAMPO OCULTO 'accion':
                 Su valor ("registro") le dice al AppController que esta petición es para la acción de registro. --%>
            <input type="hidden" name="accion" value="registro">
            
            <div class="form-group">

                <label for="nombreReg">Nombre:</label>

                <%-- name="nombre_reg": Nombre del parámetro para el nombre completo.
                     value="${param.nombre_reg}": Repoblar en caso de error de registro. --%>
                <input type="text" id="nombreReg" name="nombre_reg" required value="${param.nombre_reg}">
            </div>

            <div class="form-group">
                <label for="emailReg">Email:</label>
                <input type="email" id="emailReg" name="email_reg" required value="${param.email_reg}">
            </div>

            <div class="form-group">
                <label for="passwordReg">Contraseña:</label>
                <input type="password" id="passwordReg" name="password_reg" required>
            </div>

            <div class="form-group">
                <label for="tarjetaReg">Tarjeta de crédito (opcional):</label>
                <%-- name="tarjeta_reg": Nombre del parámetro. No es 'required'. --%>
                <input type="text" id="tarjetaReg" name="tarjeta_reg" value="${param.tarjeta_reg}">
            </div>

            <%-- BOTÓN DE ENVÍO DEL FORMULARIO DE REGISTRO --%>
            <input type="submit" value="Registrarse" class="btn btn-register">
        </form>
        <div class="link-container">
            <a href="${pageContext.request.contextPath}/index.jsp">Volver a la tienda</a>
        </div>
    </div>
</body>
</html>