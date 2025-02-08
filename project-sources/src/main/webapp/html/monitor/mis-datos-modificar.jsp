<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="VO.UsuarioVO" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"> <!-- Codificación de caracteres en UTF-8 -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> <!-- Hace que la página sea responsive -->
    <title>Mis datos - Powerfit</title> <!-- Título de la página -->
    <link rel="icon" href="/POWERFIT/images/gimnasio.png"> <!-- Icono de la pestaña -->
    <link rel="stylesheet" href="/POWERFIT/css/styles.css"> <!-- Enlace al archivo CSS externo -->
</head>
<body>

    <!-- Panel lateral -->
    <div class="sidebar" id="sidebar"> <!-- Contenedor del panel lateral -->
        <span class="close-btn" id="closeBtn" onclick="toggleSidebar()">&#10005;</span> <!-- Botón de cierre (cruz) para el panel lateral -->
        <img src="/POWERFIT/images/logo.jpg" alt="Power Fit Logo"> <!-- Imagen del logo -->
        <a href="/POWERFIT/html/monitor/home.jsp">MI CUENTA</a> <!-- Enlace a "Mi Cuenta" -->
        <a href="/POWERFIT/html/monitor/mis-datos-ver.jsp" class="active">MIS DATOS</a> <!-- Enlace a "Mis Datos" -->
        <a href="/POWERFIT/monitor/ListarClases">MIS CLASES</a> <!-- Enlace a "Mis clases" -->
    </div>

    <!-- Contenido principal -->
    <div class="main-content"> <!-- Contenedor principal que engloba el contenido -->

        <!-- Encabezado -->
        <div class="header"> <!-- Contenedor del encabezado -->
            <span class="hamburger" onclick="toggleSidebar()">&#9776;</span> <!-- Botón de menú hamburguesa -->
            <div class="header-titulo">
                <span class="titulo">POWER FIT</span> <!-- Logo "POWER FIT" -->
            </div>            
        </div>

        <!-- Sección home (titulo, subtitulo y botones) -->
        <!-- Contenedor para título y subtítulo, especial por ser el home del cliente -->
        <div class="title-container">
            <h1 class="page-title">MIS DATOS</h1>
            <br>
            <p class="page-subtitle">Aquí podrás modificar tus datos personales.</p>
        </div>

        <form action="/POWERFIT/monitor/ModificarDatos" method="post" class="login-reg-form">
        <% HttpSession session1 = request.getSession(false);
            UsuarioVO usuario = (UsuarioVO) session1.getAttribute("usuario"); %>
            
            <div class="input-group">
                <label for="nombre">Nombre</label>
                <div class="input-field">
                    <input type="nombre" id="nombre" name="nombre" value="<%= usuario.getNombre() %>" required>
                    <i class="icon-nombre"></i>
                </div>
            </div>

            <div class="input-group">
                <label for="apellidos">Apellidos</label>
                <div class="input-field">
                    <input type="apellidos" id="apellidos" name="apellidos" value="<%= usuario.getApellidos() %>" required>
                    <i class="icon-nombre"></i>
                </div>
            </div>

            <div class="input-group">
                <label for="email">Correo electrónico (no es modificable)</label>
                <div class="input-field">
                    <input type="email" id="email" name="email" value="<%= usuario.getCorreo() %>" readonly>
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
      		
      		<!-- Campo oculto con el tipo del cliente -->
          	<input type="hidden" name="tipo" value="<%= usuario.getTipo() %>">

            <div> 
            	<button type="button" class="delete-account-btn" onclick="window.location.href='/POWERFIT/html/monitor/mis-datos-ver.jsp'">CANCELAR</button> <!-- Botón de cancelar -->
            	<button type="submit" class="logout-btn">GUARDAR</button>
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

    <!-- Script para mostrar/ocultar el panel lateral -->
    <script>
        function toggleSidebar() {
            const sidebar = document.getElementById('sidebar'); // Obtiene el panel lateral por su ID
            sidebar.classList.toggle('open'); // Alterna la clase "open" en el panel lateral
        }
    </script>

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