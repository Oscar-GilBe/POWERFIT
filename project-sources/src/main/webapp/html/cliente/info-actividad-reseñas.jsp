<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="VO.ActividadVO" %>
<%@ page import="VO.ResenaVO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Base64" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	//Recuperar los datos enviados desde el JSP original
	ActividadVO actividad = (ActividadVO) request.getAttribute("actividad");
	Integer puntuacion = (Integer) request.getAttribute("puntuacion");
	List<String> horario = (List<String>) request.getAttribute("horario");
	List<ResenaVO> listaResenas = (List<ResenaVO>) request.getAttribute("listaResenas");;
	
	if (actividad == null) {
	    request.getSession().setAttribute("error", "Selecciona una actividad para ver su informacion y sus reseñas");
	    response.sendRedirect("/POWERFIT/cliente/ListarActividades");
	    return;
	}
	if (puntuacion == null) {
	    request.getSession().setAttribute("error", "Error no se ha recibido la puntuacion de la actividad\nIntentalo mas tarde");
	    response.sendRedirect("/POWERFIT/cliente/ListarActividades");
	    return;
	}
	if (horario == null) {
	    request.getSession().setAttribute("error", "Error no se ha recibido el horario de la actividad\nIntentalo mas tarde");
	    response.sendRedirect("/POWERFIT/cliente/ListarActividades");
	    return;
	} 
	if (listaResenas == null) {
	    request.getSession().setAttribute("error", "Error no se han recibido las reseñas de la actividad\nIntentalo mas tarde");
	    response.sendRedirect("/POWERFIT/cliente/ListarActividades");
	    return;
	} 
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"> <!-- Codificación de caracteres en UTF-8 -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> <!-- Hace que la página sea responsive -->
    <title>Info Actividad - Powerfit</title> <!-- Título de la página -->
    <link rel="icon" href="/POWERFIT/images/gimnasio.png"> <!-- Icono de la pestaña -->
    <link rel="stylesheet" href="/POWERFIT/css/styles.css"> <!-- Enlace al archivo CSS externo -->
</head>
<style>
    /* Estilos heredados de info-actividad.jsp */
    .actividad-detalle {
        padding: 20px;
        background-color: #fff;
        border: 1px solid #ddd;
        border-radius: 8px;
        max-width: 800px;
        margin: 20px auto;
        display: flex;
        gap: 20px;
    }

    .actividad-detalle ul {
        list-style: none;
        padding: 0;
    }
    .actividad-detalle ul li {
        margin: 10px 0;
        color: #333;
    }

    .actividad-imagen img {
        max-width: 250px; /* Limita el tamaño de la imagen */
        border-radius: 8px;
        width: 100%;
        height: auto;
        margin-left: 60px;
        margin-top: 80px;
    }

    .horarios {
        display: flex;
        flex-direction: column;
        margin-left: 20px;
        font-weight: normal;
        color: #555;
        line-height: 1.6;
    }

    .action-buttons {
        text-align: center; /* Centra los botones horizontalmente */
        margin-top: 20px; /* Añade espacio en la parte superior de los botones */
    }

    .opinion-button {
        padding: 10px 20px;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        color: white;
        background-color: orange;
        font-weight: bold;
        margin: 5px; /* Añade espacio entre cada botón */
    }

    .star {
        color: gray;
        font-size: 20px;
    }

    .star.filled {
        color: gold;
    }

    /* Espaciado entre la información de la actividad y los botones */
    .actividad-info {
        margin-bottom: 20px;
    }

    /* Para mostrar el nombre de la actividad en mayúsculas en el encabezado */
    .title-container h1 {
        t
</style>
<body>
    <!-- Panel lateral -->
    <div class="sidebar" id="sidebar"> <!-- Contenedor del panel lateral -->
        <span class="close-btn" id="closeBtn" onclick="toggleSidebar()">&#10005;</span> <!-- Botón de cierre (cruz) para el panel lateral -->
        <img src="/POWERFIT/images/logo.jpg" alt="Power Fit Logo"> <!-- Imagen del logo -->
        <a href="/POWERFIT/html/cliente/home.jsp">MI CUENTA</a> <!-- Enlace a "Mi Cuenta" -->
        <a href="/POWERFIT/html/cliente/mis-datos-ver.jsp">MIS DATOS</a> <!-- Enlace a "Mis Datos" -->
        <a href="/POWERFIT/cliente/ListarReservas">MIS RESERVAS</a> <!-- Enlace a "Mis Reservas" -->
        <a href="/POWERFIT/cliente/ListarReseñas">MIS RESEÑAS</a> <!-- Enlace a "Mis Reseñas" -->
        <hr class="divider"> <!-- Línea divisora -->
        <a href="/POWERFIT/cliente/ListarActividades" class="active">VER ACTIVIDADES</a> <!-- Enlace a "Mis Reseñas" -->
    </div>

    <!-- Contenido principal -->
    <div class="main-content">
        <!-- Encabezado -->
        <div class="header">
            <span class="hamburger" onclick="toggleSidebar()">&#9776;</span>
            <div class="header-titulo">
                <span class="titulo">POWER FIT</span>
            </div>
        </div>
    
        <!-- Sección de actividades -->
        <section>
            <!-- Encabezado de la sección -->
            <div class="title-container">
                <h1 class="page-title">INFORMACIÓN DE <%= actividad.getNombre().toUpperCase() %></h1>
                <p class="page-subtitle">Aquí encontrarás toda la información de la actividad ${actividad.nombre}.</p>
                <p class="page-subtitle">¡Lee lo que opinan otros usuarios o mira las clases disponibles!</p>
            </div>
           
            <!-- Detalle de la actividad -->
            <div class="actividad-detalle">
                <!-- Información de la actividad -->
                <div class="actividad-info">
                    <h2>Actividad: ${actividad.nombre}</h2>
                    <p>Puntuación Media:
                        <c:forEach var="i" begin="1" end="5">
                            <span class="star ${i <= puntuacion ? 'filled' : ''}">&#9733;</span>
                        </c:forEach>
                    </p>
                    <ul>
                        <li><strong>Plazas ofertadas:</strong> ${actividad.plazas}</li>
                        <li><strong>Horario:</strong>
                            <div class="horarios">
                                <c:forEach var="hora" items="${horario}">
                                    <span>${hora}</span>
                                </c:forEach>
                            </div>
                        </li>
                        <li><strong>Nivel de dificultad:</strong> ${actividad.dificultad}</li>
                        <li><strong>Material necesario:</strong> ${actividad.material}</li>
                        <li><strong>Sala asignada:</strong> ${actividad.sala}</li>
                        <li><strong>Grupo muscular a trabajar:</strong> ${actividad.grupo_muscular}</li>
                    </ul>

                    <!-- Botones de acción -->
                    <div class="action-buttons">
                        <form action="/POWERFIT/cliente/ListarClasesActividad" method="post" style="display:inline;">
                            <input type="hidden" name="actividad" value="${actividad.nombre}">
                            <button type="submit" class="opinion-button" style="font-weight: bold;">Ver clases</button>
                        </form>
                        <form action="/POWERFIT/cliente/InfoActividad" method="get" style="display:inline;">
                        	<input type="hidden" name="actividad" value="${actividad.nombre}">
                            <button type="submit" class="opinion-button" style="font-weight: bold;">Ocultar opiniones</button>
                        </form>
                        <form action="/POWERFIT/html/cliente/reseña-crear.jsp" method="get" style="display:inline;">
		                    <input type="hidden" name="actividad" value="${actividad.nombre}">
		                    <button type="submit" class="opinion-button" style="font-weight: bold;">Crear reseña</button>
		                </form>
                    </div>
                </div>

                <!-- Imagen de la actividad -->
                <div class="actividad-imagen">
                    <c:if test="${not empty imagenBase64}">
					    <img src="data:image/jpeg;base64,${imagenBase64}" alt="${actividad.nombre}" />
					</c:if>
                </div>
            </div>
            
            <!-- Lista de reseñas -->
            <section>
                <%
                    if (!listaResenas.isEmpty()) {
                        for (ResenaVO resena : listaResenas) {
                %>
                    <div class="review-container-solo-lect">
                        <h3><%= resena.getCliente() %></h3>
                        <div class="rating">
                            <% 
                                int puntuacionResena = resena.getPuntuacion(); 
                            %>
	                      	<c:forEach var="i" begin="1" end="5">
	                       		<span class="star ${i <= puntuacion ? 'filled' : ''}">&#9733;</span>
	                        </c:forEach>
                        </div>
                        <p class="review-text-solo-lect"><%= resena.getTexto() %></p>
                    </div>
                <% 
                        }
                    } else { 
                %>
                    <p>No hay reseñas disponibles para esta actividad.</p>
                <% 
                    }
                %>
            </section>
        </section>
        
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
            const sidebar = document.getElementById('sidebar');
            sidebar.classList.toggle('open');
        }
    </script>

</body>
</html>
