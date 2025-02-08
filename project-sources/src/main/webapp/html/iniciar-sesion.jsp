<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"> <!-- Codificación de caracteres en UTF-8 -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> <!-- Hace que la página sea responsive -->
    <title>Iniciar Sesión - Powerfit</title> <!-- Título de la página -->
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

        <form action="/POWERFIT/IniciarSesion" method="post" class="login-reg-form ">
            <h2>INICIAR SESIÓN</h2>
            <p>Inicia sesión introduciendo tu correo electrónico y contraseña.</p>
            
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

            <!--   <div class="forgot-password">             -->
            <!--   <a href="#">¿Olvidaste la contraseña?</a> -->
            <!--   </div>                                    -->

            <button type="submit" class="btn-submit-login">Iniciar sesión</button>

            <div class="link">
                <p>¿Todavía no tienes cuenta? <a href="registrarse.jsp">Regístrate ahora</a></p>
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