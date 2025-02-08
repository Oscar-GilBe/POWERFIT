<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="VO.UsuarioVO" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"> <!-- CodificaciÃ³n de caracteres en UTF-8 -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> <!-- Hace que la pÃ¡gina sea responsive -->
    <title>Home - Powerfit</title> <!-- TÃ­tulo de la pÃ¡gina -->
    <link rel="icon" href="/POWERFIT/images/gimnasio.png"> <!-- Icono de la pestaÃ±a -->
    <link rel="stylesheet" href="/POWERFIT/css/styles.css"> <!-- Enlace al archivo CSS externo -->
</head>
<body>

    <!-- Panel lateral -->
    <div class="sidebar" id="sidebar"> <!-- Contenedor del panel lateral -->
        <span class="close-btn" id="closeBtn" onclick="toggleSidebar()">&#10005;</span> <!-- Botón de cierre (cruz) para el panel lateral -->
        <img src="/POWERFIT/images/logo.jpg" alt="Power Fit Logo"> <!-- Imagen del logo -->
        <a href="/POWERFIT/html/administrador/home.jsp" class="active">MI CUENTA</a> <!-- Enlace a "Mi Cuenta" -->
        <a href="/POWERFIT/administrador/ListarActividades">ACTIVIDADES</a> <!-- Enlace a "Actividades" -->
        <a href="/POWERFIT/administrador/ListarMonitores">MONITORES</a> <!-- Enlace a "Monitores" -->
        <a href="/POWERFIT/administrador/ListarSalas">SALAS</a> <!-- Enlace a "Salas" -->
        <a href="/POWERFIT/administrador/Estadisticas">ESTADÍSTICAS</a> <!-- Enlace a "Estadísticas" -->
    </div>

    <!-- Contenido principal -->
    <div class="main-content"> <!-- Contenedor principal que engloba el contenido -->

        <!-- Encabezado -->
        <div class="header"> <!-- Contenedor del encabezado -->
            <span class="hamburger" onclick="toggleSidebar()">&#9776;</span> <!-- BotÃ³n de menÃº hamburguesa -->
            <div class="header-titulo">
                <span class="titulo">POWER FIT</span> <!-- Logo "POWER FIT" -->
            </div>
        </div>

        <!-- SecciÃ³n home (titulo y subtitulo) -->
        <!-- Contenedor para tÃ­tulo y subtÃ­tulo, especial por ser el home del administrador -->
        <div class="title-container">
            <h1 class="page-title-home">
            <%
    				// Verificamos si el usuario está en la sesión
    				if (request.getSession().getAttribute("usuario") == null) {
        				// Si no existe el atributo usuario, redirigimos a la página de inicio de sesión
        				response.sendRedirect("/POWERFIT/html/iniciar-sesion.jsp");
    				} else {
        			// Si el usuario está en la sesión, mostramos el nombre
				%>
		        ¡HOLA<br>
		        <%= ((UsuarioVO)request.getSession().getAttribute("usuario")).getNombre().toUpperCase() %>!
			<%
    			}
			%>
            </h1>
            <br><br>
            <p class="page-subtitle-home">Administra toda la gestión del gimnasio Power-Fit<br>¡Vamos allá!</p>
            <br>
        </div>
        
        <!-- Botones -->
        <div class="action-buttons-home">
        	<form action="/POWERFIT/administrador/CerrarSesion" method="get" id="logoutForm">
    			<button type="submit" class="logout-btn" 
    					onclick="return confirm('¿Estás seguro de que deseas cerrar sesión?');">
    					CERRAR SESIÓN
    			</button>
			</form>
        </div>
        
    </div>

    <!-- Script para mostrar/ocultar el panel lateral -->
    <script>
        function toggleSidebar() {
            const sidebar = document.getElementById('sidebar'); // Obtiene el panel lateral por su ID
            sidebar.classList.toggle('open'); // Alterna la clase "open" en el panel lateral
        }
    </script>

</body>
</html>
