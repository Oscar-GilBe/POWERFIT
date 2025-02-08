<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="VO.ActividadVO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Base64" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    // Recuperar los datos enviados desde el JSP original
    ActividadVO actividad = (ActividadVO) request.getAttribute("actividad");
    Integer puntuacion = (Integer) request.getAttribute("puntuacion");
    List<String> horario = (List<String>) request.getAttribute("horario");
    
    if (actividad == null) {
        request.getSession().setAttribute("error", "Selecciona una actividad para ver su informacion");
        response.sendRedirect("/POWERFIT/administrador/ListarActividades");
        return;
    }
    if (puntuacion == null) {
        request.getSession().setAttribute("error", "Error no se ha recibido la puntuacion de la actividad\nIntentalo mas tarde");
        response.sendRedirect("/POWERFIT/administrador/ListarActividades");
        return;
    }
    if (horario == null) {
        request.getSession().setAttribute("error", "Error no se ha recibido el horario de la actividad\nIntentalo mas tarde");
        response.sendRedirect("/POWERFIT/administrador/ListarActividades");
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
	        flex: 1;
	        text-align: right;
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
        	display: flex;
		    justify-content: space-around; /* Espacio uniforme a cada lado de los botones */
		    align-items: center; /* Alinea verticalmente */
		    width: 100%; /* Ocupa todo el ancho del contenedor */
		    height: 60px; /* Altura uniforme */
		    gap: 20px; /* Espacio entre cada botón */
		    margin-top: 80px;
        }
        
        .action-buttons button {
		    padding: 10px 20px;
		    border: none;
		    border-radius: 5px;
		    cursor: pointer;
		    color: white;
		    font-weight: bold;
		    font-size: 14px;
		    display: flex;
		    align-items: center;
		    justify-content: center;
		    line-height: 1;
		    box-sizing: border-box;
		}
		
		/* Estilo de botones individuales */
		.delete-btn {
		    width: 120px;
		    height: 100%; /* Hace que ocupe toda la altura de .action-buttons */
		}
		
		.opinion-button {
		    background-color: orange;
		    width: 120px;
		    height: 100%; /* Altura completa */
		}
		
		.edit-btn {
		    width: 120px; /* Más pequeño que los otros */
		    height: 100%; /* Altura completa */
		    font-size: 12px; /* Fuente más pequeña */
		}
        
        .button-container {
	        flex: 1; /* Asegura que cada botón ocupe su espacio */
	    }
	    .star {
	        color: gray;
	        font-size: 20px;
	    }
	
	    .star.filled {
	        color: gold;
	    }
</style>
<body>
    <!-- Panel lateral -->
    <div class="sidebar" id="sidebar"> <!-- Contenedor del panel lateral -->
        <span class="close-btn" id="closeBtn" onclick="toggleSidebar()">&#10005;</span> <!-- Botón de cierre (cruz) para el panel lateral -->
        <img src="/POWERFIT/images/logo.jpg" alt="Power Fit Logo"> <!-- Imagen del logo -->
        <a href="/POWERFIT/html/administrador/home.jsp">MI CUENTA</a> <!-- Enlace a "Mi Cuenta" -->
        <a href="/POWERFIT/administrador/ListarActividades" class="active">ACTIVIDADES</a> <!-- Enlace a "Actividades" -->
        <a href="/POWERFIT/administrador/ListarMonitores">MONITORES</a> <!-- Enlace a "Monitores" -->
        <a href="/POWERFIT/administrador/ListarSalas">SALAS</a> <!-- Enlace a "Salas" -->
        <a href="/POWERFIT/administrador/Estadisticas">ESTADÍSTICAS</a> <!-- Enlace a "Estadísticas" -->
    </div>


    <!-- Contenido principal -->
    <div class="main-content">
     <!-- Encabezado -->
        <div class="header"> <!-- Contenedor del encabezado -->
            <span class="hamburger" onclick="toggleSidebar()">&#9776;</span> <!-- Botón de menú hamburguesa -->
            <div class="header-titulo">
                <span class="titulo">POWER FIT</span> <!-- Logo "POWER FIT" -->
            </div>
        </div>
    
  		 <!-- Sección de actividades -->
        <section>
            <!-- Encabezado de la sección -->
            <div class="title-container">
                <h1 class="page-title">INFORMACIÓN DE <%= actividad.getNombre().toUpperCase() %></h1>
                <p class="page-subtitle">Aquí podrás gestionar la actividad ${actividad.nombre}.</p>
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
	                    <li><strong>Plazas disponibles:</strong> ${actividad.plazas}</li>
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
                       <form action="/POWERFIT/administrador/EliminarActividad" method="post" style="display:inline;">
                            <input type="hidden" name="actividad" value="${actividad.nombre}">
                            <button type="submit" class="delete-btn" onclick="return confirm('¿Estás seguro de que deseas eliminar esta actividad?');">
                                <img src="/POWERFIT/images/eliminar.png" alt="Eliminar">
                            </button>
                        </form>		                
	                    <form action="/POWERFIT/administrador/ListarReseñasActividad" method="get" style="display:inline;">
	                        <input type="hidden" name="actividad" value="${actividad.nombre}">
	                        <button type="submit" class="opinion-button">Ver opiniones</button>
	                    </form>		               
	               		<form action="/POWERFIT/administrador/CargarModificarActividad" method="get" style="display:inline;">
				            <input type="hidden" name="actividad" value="${actividad.nombre}">
				            <button type="submit" class="edit-btn" style="font-weight: bold;">
								EDITAR
					            <img src="/POWERFIT/images/editar.png" alt="Editar" class="icon">
				            </button>
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
