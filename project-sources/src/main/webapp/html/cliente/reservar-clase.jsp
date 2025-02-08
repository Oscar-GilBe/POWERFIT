<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, java.util.ArrayList, java.util.Map, java.util.HashMap, java.util.Calendar" %>
<%@ page import="java.time.LocalTime, java.time.LocalDate, java.time.DayOfWeek, java.time.format.DateTimeFormatter,java.text.SimpleDateFormat" %>
<%@ page import="VO.ClaseVO" %>
<%
    List<ClaseVO> clases = (List<ClaseVO>) request.getAttribute("clases");
	if (clases == null) clases = new ArrayList<>();
    LocalDate fechaInicio = (LocalDate) request.getAttribute("fechaInicio");
    LocalDate fechaFin = (LocalDate) request.getAttribute("fechaFin");
    LocalDate fechaActual = LocalDate.now(); // Fecha actual
    DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter sdf_str = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    String nombreActividad = (String) request.getAttribute("actividad");
    
    if (nombreActividad == null) {
        request.getSession().setAttribute("error", "Selecciona una actividad para reservar una clase");
        response.sendRedirect("/POWERFIT/cliente/ListarActividades");
        return;
    }

    // Crear un mapa de clases por día y hora
    Map<DayOfWeek, Map<LocalTime, ClaseVO>> horarioClases = new HashMap<>();

    // Rellenar el mapa con las clases
    for (ClaseVO clase : clases) {
        DayOfWeek dia = clase.getDia();
        LocalTime inicio = clase.getInicio();
        
        horarioClases.putIfAbsent(dia, new HashMap<>());
        horarioClases.get(dia).put(inicio, clase);
    }

    // Crear las horas que quieres mostrar en la tabla (de 8:00 a 18:00)
    LocalTime[] horas = new LocalTime[12];
    for (int i = 0; i < 12; i++) {
        horas[i] = LocalTime.of(8 + i, 0); // 08:00, 09:00, ..., 18:00
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reservar clase - Powerfit</title>
    <link rel="icon" href="/POWERFIT/images/gimnasio.png">
    <link rel="stylesheet" href="/POWERFIT/css/styles.css">
    <script src="https://cdn.jsdelivr.net/npm/date-fns@4.1.0/cdn.min.js"></script>
</head>
<style>
    .container { max-width: 800px; margin: 0 auto; padding: 20px; }
    .form-group { margin-bottom: 15px; display: flex; align-items: center; }
    label { width: 150px; font-weight: bold; }
    input[type="text"], select { width: 100%; padding: 8px; margin-left: 10px; }
    .day-row { display: flex; align-items: center; margin-bottom: 10px; }
    .hours-group { display: flex; gap: 10px; }
    .add-time-btn { background-color: #5cb85c; color: white; border: none; padding: 5px 10px; cursor: pointer; }
    .error { color: red; font-size: 0.9em; }
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

        <section>
            <div class="title-container">
                <h1 class="page-title">CLASES DE <%= nombreActividad.toUpperCase() %></h1>
                <p class="page-subtitle">Aquí aparecen las clases disponibles de <%= nombreActividad %>. ¡Prueba a reservar una!</p>
            </div>

            <div class="schedule-container-monitor">
                <div class="week-day-switcher">
                    <button id="prevBtn" onclick="cargarClases('anterior')">Anterior</button>
                    <span id="week-or-day-title"> desde <%= fechaInicio.format(sdf_str) %> hasta <%= fechaFin.format(sdf_str) %></span>
                    <button id="nextBtn" onclick="cargarClases('siguiente')">Siguiente</button>
                </div>
            
                <table class="schedule-table-monitor">
                    <thead>
                        <tr>
                            <th>HORA</th>
                            <th>LUNES</th>
                            <th>MARTES</th>
                            <th>MIERCOLES</th>
                            <th>JUEVES</th>
                            <th>VIERNES</th>
                            <th>SABADO</th>
                            <th>DOMINGO</th>
                        </tr>
                    </thead>
                    <tbody id="schedule-body">
                        <%
			                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
			                boolean clasesDisponibles = false;
			                
			                // Iterar sobre las horas de 08:00 a 18:00
			                for (LocalTime hora : horas) {
			            %>
			            <tr>
			                <td><%= hora.format(timeFormatter) %></td>
			                
			                <% // Iterar sobre los días de la semana
			                for (DayOfWeek dia : DayOfWeek.values()) {
			                    if (dia.getValue() >= 1 && dia.getValue() <= 7) { // De Lunes a Domingo
			                        ClaseVO clase = horarioClases.getOrDefault(dia, new HashMap<>()).get(hora);
			
			                        if (clase != null) {
			                            clasesDisponibles = true; // Indica que hay al menos una clase disponible
			                %>
							    <td>
							        <form action="/POWERFIT/cliente/ReservarClase" method="post">
									    <input type="hidden" name="actividad" value="<%= clase.getActividad() %>">
									    <input type="hidden" name="fecha" value="<%= clase.getFecha() %>">
									    <input type="hidden" name="inicio" value="<%= clase.getInicio() %>">
									    <div class="activity-container" style="display: flex; justify-content: space-between; align-items: center;">
									        <!-- Contenedor para el nombre y las plazas disponibles, centrado en la columna -->
									        <div style="display: flex; flex-direction: column; align-items: center;">
									            <span>Plazas: <%= clase.getPlazas() %></span>
									        </div>
									        
									        <% if (clase.getFecha().isAfter(fechaActual) && clase.getPlazas() > 0) { %>
									            <!-- Botón de reserva a la derecha -->
									            <button type="submit" class="delete-btn-calendar" 
									                    onclick="return confirm('¿Quieres reservar una clase de <%= clase.getActividad() %> para el dia <%= clase.getFecha().format(sdf_str) %> a las <%= clase.getInicio().format(timeFormatter) %>?');">
									                <img src="/POWERFIT/images/reservar.png" alt="Reservar">
									            </button>
									        <% } %>
									    </div>
									</form>

							    </td>
							<% 
			                        } else {
			                %>
			                    <td></td>
			                <%
			                        }
			                    }
			                }
			                %>
			            </tr>
			            <%
			                }
			            %>

			            <!-- Si no hay clases disponibles, mostrar un mensaje -->
			            <% if (!clasesDisponibles) { %>
			                <tr>
			                    <td colspan="8" style="text-align: center; font-style: italic; color: gray;">
			                        No hay clases disponibles esta semana.
			                    </td>
			                </tr>
			            <% } %>
                    </tbody>
                </table>
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
        </section>
    </div>

    <!-- JavaScript -->
    <!-- Script para mostrar/ocultar el panel lateral -->
    <script>
	    function cargarClases(accion) {
	    	const nombreActividad = "<%= nombreActividad %>";
	        const fechaInicioStr = "<%= fechaInicio.format(sdf) %>";
	        const fechaFinStr = "<%= fechaFin.format(sdf) %>";
	
	        // Convertir las fechas a objetos Date
	        let fechaInicio = new Date(fechaInicioStr);
	        let fechaFin = new Date(fechaFinStr);
	        
	        // Base de la URL con parámetros de fecha
	        let url = "/POWERFIT/cliente/ListarClasesActividad?actividad=" + nombreActividad + "&fechaInicio=" + fechaInicioStr + "&fechaFin=" + fechaFinStr;
	
	        // Agregar el parámetro de acción a la URL dependiendo de la acción seleccionada
	        if (accion === 'siguiente') {
	            url += "&accion=siguiente";
	        } else if (accion === 'anterior') {
	            url += "&accion=anterior";
	        }
			console.log(nombreActividad);
			console.log(url);
	        // Redirigir a la URL construida
	        window.location.href = url;
	    }
	        
        function toggleSidebar() {
            const sidebar = document.getElementById('sidebar');
            sidebar.classList.toggle('open');
        }
    </script>

</body>
</html> 