<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="VO.ActividadVO" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"> <!-- Codificación de caracteres en UTF-8 -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> <!-- Hace que la página sea responsive -->
    <title>Actividades - Powerfit</title> <!-- Título de la página -->
    <link rel="icon" href="/POWERFIT/images/gimnasio.png"> <!-- Icono de la pestaña -->
    <link rel="stylesheet" href="/POWERFIT/css/styles.css"> <!-- Enlace al archivo CSS externo -->
</head>
<style>
    /* Estilos de la barra de búsqueda */
    .search-bar {
        display: flex;
        align-items: center;
        margin-bottom: 1rem;
        background-color: #eee;
        padding: 0.5rem;
        max-width: 800px;
        margin-left:75px;
    }
    .search-bar input {
        flex: 1;
        border: none;
        padding: 0.5rem;
        margin-right: 0.5rem;
    }
    .search-bar button {
        background-color: #bbb;
        border: none;
        padding: 0.5rem 1rem;
        cursor: pointer;
    }
    
    /* Botón de agregar monitor */
	#addActividadBtn {
		margin-top: 20px; /* Espacio entre la parte superior y el botón */
	    display: flex;
	    align-items: center;
	    background-color: #007bff;
	    color: white;
	    border: none;
	    border-radius: 50px;
	    cursor: pointer;
	    font-size: 18px;
	    margin-bottom: 20px;
	    padding: 15px 30px;
	}
	
	#addActividadBtn:hover {
	    background-color: #0056b3; /* Color al pasar el cursor */
	}
	
	#addActividadBtn::before {
	    content: "+";
	    display: inline-block;
	    font-size: 24px;
	    margin-right: 10px;
	}
	
	.actividad-link {
		position: absolute;  /* Posiciona el botón absolutamente dentro de .actividad-item */
	    top: 0;
	    left: 0;
	    width: 100%;         /* Que ocupe todo el ancho */
	    height: 100%;        /* Que ocupe toda la altura */
	    background: transparent; /* Fondo transparente para no interferir con el diseño */
	    border: none;
	    cursor: pointer;     /* Cambia el cursor a "mano" al pasar por encima */
	    z-index: 1;          /* Asegúrate de que esté por encima del contenido */
    }
    
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
    <div class="main-content"> <!-- Contenedor principal que engloba el contenido -->

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
                <h1 class="page-title">ACTIVIDADES</h1>
                <p class="page-subtitle">Aquí encontrarás las actividades que ofrece ahora mismo el gimnasio.</p>
            </div>
           
            <form action="/POWERFIT/cliente/ListarActividades" method="get">
	             <!-- Filtros para ordenar actividades -->
	            <div class="filters">
	                <h3>Filtrar por:</h3>
					    <!-- Botones que envían el parámetro ordenarPor -->
					    <button type="submit" name="ordenarPor" value="nombre" class="${param.ordenarPor == 'nombre' || param.ordenarPor == null ? 'button-active' : ''}">Nombre</button>
				        <button type="submit" name="ordenarPor" value="grupo_muscular" class="${param.ordenarPor == 'grupo_muscular' ? 'button-active' : ''}">Grupo Muscular</button>
				        <button type="submit" name="ordenarPor" value="dificultad" class="${param.ordenarPor == 'dificultad' ? 'button-active' : ''}">Dificultad</button>
				        <button type="submit" name="ordenarPor" value="puntuacion" class="${param.ordenarPor == 'puntuacion' ? 'button-active' : ''}">Puntuación</button>
				        <button type="submit" name="ordenarPor" value="sala" class="${param.ordenarPor == 'sala' ? 'button-active' : ''}">Sala</button>
	            </div>
	            
            	<!-- Barra de búsqueda -->
	            <div class="search-bar">
	               		 <!-- Campo oculto para mantener el estado de ordenarPor -->
	                    <input type="text" id="searchInput" name="search" placeholder="Buscar..." value="${searchQuery}" />
	                    <button type="submit" id="searchButton" name="ordenarPor" value="${param.ordenarPor}">Buscar</button>
	            </div>
	        </form>
            
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


            <!-- Verificar si las actividades están agrupadas -->
            <c:if test="${agrupadas}">
                <!-- Mostrar las actividades agrupadas -->
                <c:forEach var="entry" items="${grupo}">
                    <h3>${entry.key}</h3> <!-- Mostrar el nombre del grupo (puntuación, grupo muscular, etc.) -->

                    <div class="actividad-list">
                        <c:forEach var="actividad" items="${entry.value}">
                            <div class="actividad-item">
                            	<form action="/POWERFIT/cliente/InfoActividad" method="get">
                            		<input type="hidden" name="actividad" value="${actividad.nombre}" />
                            		<button type="submit" class="actividad-link"></button>
                            	</form>
                            	
								<!-- Obtener la imagen Base64 asociada a la actividad -->
						        <c:choose>
						            <c:when test="${not empty imagenesBase64[actividad.nombre]}">
						                <img src="data:image/jpeg;base64,${imagenesBase64[actividad.nombre]}" alt="${actividad.nombre}" />
						            </c:when>
						            <c:otherwise>
						                <p>Imagen no disponible</p>
						            </c:otherwise>
						        </c:choose>
						        
                                <p>Nombre: ${actividad.nombre}</p>
                                <p>Grupo Muscular: ${actividad.grupo_muscular}</p>
                                <p>Dificultad: ${actividad.dificultad}</p>
                                <p>Sala: ${actividad.sala}</p>
                            </div>
                        </c:forEach>
                    </div>
                    <br>
                </c:forEach>
            </c:if>

            <!-- Si no están agrupadas, mostrar la lista simple -->
            <c:if test="${not agrupadas}">
			    <div class="actividad-list">
			        <c:forEach var="actividad" items="${listaActividades}">
			            <div class="actividad-item">
			           		<form action="/POWERFIT/cliente/InfoActividad" method="get">
			           			<input type="hidden" name="actividad" value="${actividad.nombre}" />
			           			<button type="submit" class="actividad-link"></button>
			           		</form>

							<!-- Obtener la imagen Base64 asociada a la actividad -->
					        <c:choose>
					            <c:when test="${not empty imagenesBase64[actividad.nombre]}">
					                <img src="data:image/jpeg;base64,${imagenesBase64[actividad.nombre]}" alt="${actividad.nombre}" />
					            </c:when>
					            <c:otherwise>
					                <p>Imagen no disponible</p>
					            </c:otherwise>
					        </c:choose>

			                <p>Nombre: ${actividad.nombre}</p>
			                <p>Grupo Muscular: ${actividad.grupo_muscular}</p>
			                <p>Dificultad: ${actividad.dificultad}</p>
			                <p>Sala: ${actividad.sala}</p>
			            </div>
			        </c:forEach>
			    </div>
			</c:if>
            
            <br>
		<c:if test="${empty listaActividades}">
		    <p>No se encontraron actividades que coincidan con el término de búsqueda "${searchQuery}".</p>
		</c:if>
		<c:if test="${not empty listaActividades}">
		    <p>Actividades cargadas: ${listaActividades.size()}</p>
		</c:if>
        </section>
    </div>


    <!-- Script para mostrar/ocultar el panel lateral -->
    <script>
        function toggleSidebar() {
            const sidebar = document.getElementById('sidebar'); // Obtiene el panel lateral por su ID
            sidebar.classList.toggle('open'); // Alterna la clase "open" en el panel lateral
        }
        
        // Inhabilitar el envío del formulario con Enter y simular clic en el botón de búsqueda
        document.getElementById('searchInput').addEventListener('keypress', function(event) {
            if (event.key === 'Enter') {
                event.preventDefault(); // Prevenir el envío del formulario
                document.getElementById('searchButton').click(); // Simular clic en el botón de búsqueda
            }
        });
    </script>

</body>
</html>
