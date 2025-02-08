<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="VO.UsuarioVO"%>
<%
    // Recuperar los datos enviados desde el JSP original
    	String puntuacion_str = request.getParameter("puntuacion");
       String texto = request.getParameter("texto");
       String cliente = request.getParameter("cliente");
       String actividad = request.getParameter("actividad");
       
       if (cliente == null || cliente.isEmpty()) {
       		cliente = ((UsuarioVO) request.getSession().getAttribute("usuario")).getCorreo();
       }
       if (actividad == null || actividad.isEmpty()) {
       	request.getSession().setAttribute("error", "Selecciona la reseña de una actividad existente");
           response.sendRedirect("/POWERFIT/cliente/ListarResenasCliente");
           return;
       }
       if (puntuacion_str == null || puntuacion_str.isEmpty()) {
       	request.getSession().setAttribute("error", "Introduce una puntuacion");
           response.sendRedirect("/POWERFIT/cliente/ListarResenasCliente");
           return;
       }
       Integer puntuacion = Integer.parseInt(puntuacion_str);
       if (puntuacion < 0 || puntuacion > 5) {
       	request.getSession().setAttribute("error", "La puntuacion no es valida");
       	request.getRequestDispatcher("/POWERFIT/html/cliente/reseña-editar.jsp").forward(request, response);
           return;
       }
       if (texto == null || texto.isEmpty()) {
       	request.getSession().setAttribute("error", "Escribe el texto de la reseña");
       	request.getRequestDispatcher("/POWERFIT/html/cliente/reseña-editar.jsp").forward(request, response);
           return;
       }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"> <!-- Codificación de caracteres en UTF-8 -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> <!-- Hace que la página sea responsive -->
    <title>Editar reseña - PowerFit</title> <!-- Título de la página -->
    <link rel="icon" href="/POWERFIT/images/gimnasio.png"> <!-- Icono de la pestaña -->
    <link rel="stylesheet" href="/POWERFIT/css/styles.css"> <!-- Enlace al archivo CSS externo -->
</head>
<body>

    <!-- Panel lateral -->
    <div class="sidebar" id="sidebar"> <!-- Contenedor del panel lateral -->
        <span class="close-btn" id="closeBtn" onclick="toggleSidebar()">&#10005;</span> <!-- Botón de cierre (cruz) para el panel lateral -->
        <img src="/POWERFIT/images/logo.jpg" alt="Power Fit Logo"> <!-- Imagen del logo -->
        <a href="/POWERFIT/html/cliente/home.jsp">MI CUENTA</a> <!-- Enlace a "Mi Cuenta" -->
        <a href="/POWERFIT/html/cliente/mis-datos-ver.jsp">MIS DATOS</a> <!-- Enlace a "Mis Datos" -->
        <a href="/POWERFIT/cliente/ListarReservas">MIS RESERVAS</a> <!-- Enlace a "Mis Reservas" -->
        <a href="/POWERFIT/cliente/ListarReseñas" class="active">MIS RESEÑAS</a> <!-- Enlace a "Mis Reseñas" -->
        <hr class="divider"> <!-- Línea divisora -->
		<a href="/POWERFIT/cliente/ListarActividades">VER ACTIVIDADES</a> <!-- Enlace a "Mis Reseñas" -->        
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
            <h1 class="page-title">MODIFICAR RESEÑA</h1>
            <p class="page-subtitle">Aquí podrás modificar tus reseñas.</p>
        </div>

		<a><br><br></a>
		
        <div class="review-container"> <!-- Contenedor de la reseña -->
		    <h2>Actividad: <%= actividad %></h2> <!-- Título con nombre de la actividad -->
		
		    <!-- Formulario de edición de reseña -->
		    <form action="/POWERFIT/cliente/ModificarReseña" method="post" class="review-form">
		        <input type="hidden" name="cliente" value="<%= cliente %>">
		        <input type="hidden" name="actividad" value="<%= actividad %>">
		
		        <!-- Campo para editar la puntuación y su selector en la misma línea -->
		        <label for="puntuacion">Puntuación:</label>
		        <select name="puntuacion" id="puntuacion" class="puntuacion-select">
		            <option value="1" <%= puntuacion == 1 ? "selected" : "" %>>1</option>
		            <option value="2" <%= puntuacion == 2 ? "selected" : "" %>>2</option>
		            <option value="3" <%= puntuacion == 3 ? "selected" : "" %>>3</option>
		            <option value="4" <%= puntuacion == 4 ? "selected" : "" %>>4</option>
		            <option value="5" <%= puntuacion == 5 ? "selected" : "" %>>5</option>
		        </select>
				<a><br><br></a>
				
		        <!-- Campo para editar el texto de la reseña -->
		        <label for="texto">Texto de la reseña:</label>
		        <textarea name="texto" id="texto" rows="10" class="review-textarea"><%= texto %></textarea>
		        
		        <!-- Botones de Cancelar y Guardar alineados horizontalmente -->
		        <div>
		            <button type="button" class="delete-account-btn" onclick="window.location.href='/POWERFIT/cliente/ListarReseñas'">CANCELAR</button>
		            <button type="submit" class="logout-btn">GUARDAR</button>
		        </div>
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
