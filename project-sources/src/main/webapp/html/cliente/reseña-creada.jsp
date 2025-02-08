<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%
    // Recuperar los datos enviados desde el JSP original
       String actividad = request.getParameter("actividad");

       if (actividad == null || actividad.isEmpty()) {
       	   request.getSession().setAttribute("error", "Selecciona una actividad y crea una reseña");
           response.sendRedirect("/POWERFIT/cliente/ListarReseñas");
           return;
	   }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"> <!-- Codificación de caracteres en UTF-8 -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> <!-- Hace que la página sea responsive -->
    <title>Reseña creada - PowerFit</title> <!-- Título de la página -->
    <link rel="icon" href="/POWERFIT/images/gimnasio.png"> <!-- Icono de la pestaña -->
    <link rel="stylesheet" href="/POWERFIT/css/styles.css"> <!-- Enlace al archivo CSS externo -->
</head>
<body>

    <!-- Panel lateral -->
    <div class="sidebar" id="sidebar"> <!-- Contenedor del panel lateral -->
        <span class="close-btn" id="closeBtn" onclick="toggleSidebar()">&#10005;</span> <!-- Botón de cierre (cruz) para el panel lateral -->
        <img src="/POWERFIT/images/logo.jpg" alt="Power Fit Logo"> <!-- Imagen del logo -->
        <a href="#">MI CUENTA</a> <!-- Enlace a "Mi Cuenta" -->
        <a href="#">MIS DATOS</a> <!-- Enlace a "Mis Datos" -->
        <a href="#">MIS RESERVAS</a> <!-- Enlace a "Mis Reservas" -->
        <a href="#">MIS RESEÑAS</a> <!-- Enlace a "Mis Reseñas" con clase activa -->
        <hr class="divider"> <!-- Línea divisora -->
		<a href="#" class="active">VER ACTIVIDADES</a> <!-- Enlace a "Ver Actividades" -->
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

        <!-- Sección de éxito -->
        <div class="mensaje">
            <div class="icono-exito">
                <img src="/POWERFIT/images/exito.png" alt="Operación realizada con éxito">
            </div>
            <h1>¡Gracias!</h1>
            <p>Tu reseña para <%= actividad %> ha sido creada con éxito.</p>
            <p id="loadingMessage" style="display: none; font-style: italic; color: gray;">Redirigiendo... Por favor, espera un momento.</p>
        </div>

    </div>

    <!-- Script para mostrar/ocultar el panel lateral y el mensaje de carga -->
    <script>
        function toggleSidebar() {
            const sidebar = document.getElementById('sidebar'); // Obtiene el panel lateral por su ID
            sidebar.classList.toggle('open'); // Alterna la clase "open" en el panel lateral
        }

        // Mostrar mensaje de "Redirigiendo..." y luego redirigir después de 5 segundos
        setTimeout(function() {
            document.getElementById('loadingMessage').style.display = 'block'; // Muestra el mensaje de carga
        }, 500); // Muestra el mensaje de carga a los 3 segundos

        setTimeout(function() {
        	const actividad = encodeURIComponent("<%= actividad %>");
            window.location.href = "/POWERFIT/cliente/InfoActividad?actividad=" + actividad; // Redirige a la página de destino
        }, 5000); // Redirige después de 5 segundos
    </script>

</body>
</html>
