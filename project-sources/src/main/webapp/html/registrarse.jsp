<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"> <!-- Codificación de caracteres en UTF-8 -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> <!-- Hace que la página sea responsive -->
    <title>Registrarse - Powerfit</title> <!-- Título de la página -->
    <link rel="icon" href="/POWERFIT/images/gimnasio.png"> <!-- Icono de la pestaña -->
    <link rel="stylesheet" href="/POWERFIT/css/styles.css"> <!-- Enlace al archivo CSS externo -->
</head>
<body>
    <div class="main-content-sin-panel">
        <!-- Encabezado -->
        <div class="header-sin-panel"> <!-- Contenedor del encabezado -->
            <div class="header-titulo">
                <div class="logo-header">
                    <img src="/POWERFIT/images/logo.jpg" alt="PowerFit Logo">
                </div>
                <span class="titulo">POWER FIT</span> <!-- Logo "POWER FIT" -->
            </div>
        </div>

        <form action="/POWERFIT/CrearCuenta" method="post" class="login-reg-form">
            <h2>CREAR CUENTA</h2>
            <p>Crea una cuenta introduciendo tus datos personales.</p>

            <div class="input-group">
                <label for="nombre">Nombre</label>
                <div class="input-field">
                    <input type="text" id="nombre" name="nombre" placeholder="Escribe aquí tu nombre" required>
                </div>
            </div>

            <div class="input-group">
                <label for="apellidos">Apellidos</label>
                <div class="input-field">
                    <input type="text" id="apellidos" name="apellidos" placeholder="Escribe aquí tus dos apellidos" required>
                </div>
            </div>
            
            <div class="input-group">
                <label for="email">Correo electrónico</label>
                <div class="input-field">
                    <input type="email" id="email" name="email" placeholder="example@gmail.com" required>
                    <i class="icon-email"></i>
                </div>
            </div>

            <div class="input-group">
                <label for="password">Contraseña</label>
                <div class="input-field">
                    <input type="password" id="password" name="password" placeholder="********" required>
                    <button type="button" class="ver-password" onclick="togglePassword()">👁️</button>
                </div>
            </div>

            <button type="submit" class="btn-submit-login">Crear cuenta</button>

            <div class="link">
                <p>¿Ya tienes una cuenta? <a href="iniciar-sesion.jsp">Inicia sesión aquí</a></p>
            </div>

            <div class="link">
                <p>Al crear una cuenta en POWERFIT estás aceptando los <a href="">Términos de Servicio</a> y las <a href="">Políticas de Privacidad</a></p>
            </div>
            
             <!-- Mensaje de error si lo hubiera -->
            <%
                String error = (String) request.getSession().getAttribute("error");
                if (error != null && !error.isEmpty()) {
            %>
                <div class="error">
                    <p><%= error %></p>
                </div>
            <%
                	request.getSession().removeAttribute("error");
                }
            %>
        </form>
    </div>
    

    <script>
        // Función para mostrar/ocultar contraseña
        function togglePassword() {
            var passwordField = document.getElementById('password');
            var passwordType = passwordField.getAttribute('type');
            if (passwordType === 'password') {
                passwordField.setAttribute('type', 'text');
            } else {
                passwordField.setAttribute('type', 'password');
            }
        }
    </script>

</body>
</html>
