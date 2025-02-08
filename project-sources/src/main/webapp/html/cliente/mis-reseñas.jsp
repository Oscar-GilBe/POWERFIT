<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="VO.ResenaVO" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"> <!-- Codificación de caracteres en UTF-8 -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> <!-- Hace que la página sea responsive -->
    <title>Mis Reseñas - PowerFit</title> <!-- Título de la página -->
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

        <!-- Sección de reseñas -->
        <section>
            <div class="title-container">
                <h1 class="page-title">MIS RESEÑAS</h1>
                <p class="page-subtitle">Aquí podrás consultar las reseñas que has escrito.</p>
            </div>

            <!-- Contenedor de reseñas dinámicas -->
            <% 
                List<ResenaVO> listaResenas = (List<ResenaVO>) request.getAttribute("listaResenas");
                if (listaResenas != null && !listaResenas.isEmpty()) {
                    for (ResenaVO resena : listaResenas) {
            %>
                <div class="review-container-solo-lect">
                    <h2>
						<%= resena.getActividad() %> <!-- Nombre de la actividad -->
	                    <!-- Estrellas de puntuación -->
	                    <span class="rating">
	                        <% 
                            	int puntuacion = resena.getPuntuacion(); 
                            	for (int i = 1; i <= 5; i++) {
                                	if (i <= puntuacion) {
                        	%>
	                                    <span class="star full-star">&#9733;</span> <!-- Estrella llena -->
	                        <% 
                                	} else {
                        	%>
	                                    <span class="star empty-star">&#9734;</span> <!-- Estrella vacía -->
	                        <% 
                                	}
                           		} 
                        	%>
	                    </span>
					</h2> <!-- Título de la reseña -->
                    <p class="review-text-solo-lect"><%= resena.getTexto() %></p> <!-- Contenido de la reseña -->
                    <div class="action-buttons" style="display: flex; align-items: center; gap: 10px; margin-right: 35px;">
                        <!-- Formulario de edición -->
        				<form action="/POWERFIT/html/cliente/reseña-editar.jsp" method="post" style="display: inline;">
            				<!-- Campos ocultos con la información de la reseña -->
				            <input type="hidden" name="cliente" value="<%= resena.getCliente() %>">
				            <input type="hidden" name="actividad" value="<%= resena.getActividad() %>">
				            <input type="hidden" name="puntuacion" value="<%= resena.getPuntuacion() %>">
				            <input type="hidden" name="texto" value="<%= resena.getTexto() %>">
				            <button type="submit"  class="edit-btn">
				            	EDITAR
                				<img src="/POWERFIT/images/editar.png" alt="Editar" class="icon">
            				</button>
        				</form>
        				<form action="/POWERFIT/cliente/EliminarReseña" method="post" style="display: inline;">
            				<!-- Campos ocultos con la información de la reseña -->
				            <input type="hidden" name="cliente" value="<%= resena.getCliente() %>">
				            <input type="hidden" name="actividad" value="<%= resena.getActividad() %>">
				            <button type="submit" class="delete-btn" onclick="return confirm('¿Estás seguro de que deseas eliminar esta reseña?');">
                                 <img src="/POWERFIT/images/eliminar.png" alt="Eliminar">
                            </button>
        				</form>
                    </div>
                </div>
            <% 
		            }
		        } else { 
		    %>
		        <p>No hay reseñas disponibles.</p> <!-- Mensaje predeterminado si no hay reseñas -->
		    <% 
		        } 
		     
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
        </section>
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
