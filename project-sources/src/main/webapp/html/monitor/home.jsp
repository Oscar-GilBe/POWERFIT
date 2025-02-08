<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="VO.UsuarioVO" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"> <!-- Codificación de caracteres en UTF-8 -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> <!-- Hace que la página sea responsive -->
    <title>Home - Powerfit</title> <!-- Título de la página -->
    <link rel="icon" href="/POWERFIT/images/gimnasio.png"> <!-- Icono de la pestaña -->
    <link rel="stylesheet" href="/POWERFIT/css/styles.css"> <!-- Enlace al archivo CSS externo -->
</head>
<body>

    <!-- Panel lateral -->
    <div class="sidebar" id="sidebar"> <!-- Contenedor del panel lateral -->
        <span class="close-btn" id="closeBtn" onclick="toggleSidebar()">&#10005;</span> <!-- Botón de cierre (cruz) para el panel lateral -->
        <img src="/POWERFIT/images/logo.jpg" alt="Power Fit Logo"> <!-- Imagen del logo -->
        <a href="/POWERFIT/html/monitor/home.jsp" class="active">MI CUENTA</a> <!-- Enlace a "Mi Cuenta" -->
        <a href="/POWERFIT/html/monitor/mis-datos-ver.jsp">MIS DATOS</a> <!-- Enlace a "Mis Datos" -->
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

        <!-- Sección home (titulo y subtitulo) -->
        <!-- Contenedor para título y subtítulo, especial por ser el home del monitor -->
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
            <p class="page-subtitle-home">Consulta tus próximas clases en Power-Fit<br>¡Vamos allá!</p>
            <br>
        </div>
        
        <!-- Botones -->
        <div class="action-buttons-home">
        	<form action="/POWERFIT/monitor/CerrarSesion" method="get" id="logoutForm">
    			<button type="submit" class="logout-btn" 
    					onclick="return confirm('¿Estás seguro de que deseas cerrar sesión?');">
    					CERRAR SESIÓN
    			</button>
			</form>
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
