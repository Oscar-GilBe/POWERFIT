<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="VO.ActividadVO" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"> <!-- Codificación de caracteres en UTF-8 -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> <!-- Hace que la página sea responsive -->
    <title>Actividades - Crear - Powerfit</title> <!-- Título de la página -->
    <link rel="icon" href="/POWERFIT/images/gimnasio.png"> <!-- Icono de la pestaña -->
    <link rel="stylesheet" href="/POWERFIT/css/styles.css"> <!-- Enlace al archivo CSS externo -->
</head>
    <style>
        .container { max-width: 800px; margin: 0 auto; padding: 20px; }
        .form-group { margin-bottom: 15px; display: flex; align-items: center; }
        label { width: 150px; font-weight: bold; }
        input[type="text"], input[type="number"], select { width: 100%; padding: 8px; margin-left: 10px; }
    
        /* Contenedor de días */
        .day-row { display: flex; align-items: center; margin-bottom: 10px; }
        
        /* Contenedor de horarios extras */
        .hours-group { display: flex; flex-direction: column; gap: 5px; margin-left: 20px; }
        
        /* Cada fila de inicio-fin en horizontal */
    	.time-row { display: flex; gap: 10px; align-items: center; position: relative; }
    	
    	/* Botón para añadir horario extra */
        .add-time-btn { background-color: #5cb85c; color: white; border: none; padding: 5px 10px; cursor: pointer; margin-top: 10px; align-self: flex-start; }
        
        /* Botón para eliminar horarios extra */
        .remove-time-btn { background-color: #d9534f; color: white; border: none; padding: 3px 8px; cursor: pointer; margin-left: 10px; }
        
        /* Estilo para el grupo de dificultad alineado horizontalmente */
        .difficulty { display: flex; align-items: center; gap: 15px; margin-left: 10px; }
        
        input[type="checkbox"] {
        margin-right: 10px; /* Espacio entre el checkbox y el texto */
        vertical-align: middle; /* Alineación vertical con el texto */
    }
    </style>
<body>
    <!-- Panel lateral -->
    <div class="sidebar" id="sidebar"> <!-- Contenedor del panel lateral -->
        <span class="close-btn" id="closeBtn" onclick="toggleSidebar()">&#10005;</span> <!-- Botón de cierre (cruz) para el panel lateral -->
        <img src="/POWERFIT/images/logo.jpg" alt="Power Fit Logo"> <!-- Imagen del logo -->
        <a href="/POWERFIT/html/administrador/home.jsp">MI CUENTA</a> <!-- Enlace a "Mi Cuenta" -->
        <a href="/POWERFIT/administrador/ListarActividades"  class="active">ACTIVIDADES</a> <!-- Enlace a "Actividades" -->
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
                <h1 class="page-title">CREAR ACTIVIDAD</h1>
                <p class="page-subtitle">Aquí podrás crear una nueva actividad para ofrecerla en el gimnasio.</p>
            </div>
           
            <div class="container">
			<!-- Formulario para crear una nueva actividad -->
        	<form action="/POWERFIT/administrador/CrearActividad" method="post" enctype="multipart/form-data" class="form-container">
        	
            <!-- Nombre de la actividad -->
            <div class="form-group">
                <label for="nombre">Nombre:</label>
                <input type="text" id="nombre" name="nombre" placeholder="Escribe aquí el nombre de la actividad" required>
            </div>
            
            <!-- Plazas ofertadas (mínimo 1) -->
            <div class="form-group">
                <label for="plazas_ofertadas">Plazas Ofertadas:</label>
                <input type="number" id="plazas_ofertadas" name="plazas_ofertadas" min="1" placeholder="Número de plazas" required>
            </div>

            <!-- Selección de horarios -->
            <label>Horarios:</label>
            <c:forEach var="day" items="${dias}">
		    <div class="day-row">
		        <input type="checkbox" id="${day}-checkbox" name="dias[]" value="${day}" onchange="toggleDayTimes('${day}')">
		        <label for="${day}-checkbox">${day}</label>
		        <div id="${day}-times" class="hours-group" style="display: none;">
		            <!-- Primer horario obligatorio -->
		            <div class="time-row">
		                <label>Inicio:</label>
		                <input type="time" name="${day}_inicio[]" onchange="updateEndTime(this)" step="3600" min="08:00" max="19:00">
		                <label>Fin:</label>
		                <input type="time" name="${day}_fin[]" readonly>
		            </div>
		            <!-- Botón para añadir horario extra -->
		            <button type="button" class="add-time-btn" onclick="addTimeRow('${day}')">Añadir Horario</button>
		        </div>
		    </div>
			</c:forEach>
            
            <!-- Nivel de dificultad -->
            <div class="form-group">
                <label>Nivel de Dificultad:</label>
                
                <div class="difficulty">
	                <input type="radio" id="facil" name="dificultad" value="FACIL" required>
	                <label for="facil">Fácil</label>
	                
	                <input type="radio" id="media" name="dificultad" value="MEDIA" required>
	                <label for="intermedio">Media</label>
	                
	                <input type="radio" id="dificil" name="dificultad" value="DIFICIL" required>
	                <label for="experto">Difícil</label>
           		</div>
            </div>
            
            <!-- Material utilizado -->
            <div class="form-group">
                <label for="material">Material Utilizado:</label>
                <input type="text" id="material" name="material" placeholder="Escribe aquí el material utilizado" required>
            </div>
            
            <!-- Sala asignada -->
            <div class="form-group">
                <label for="sala_asignada">Sala Asignada:</label>
                <select id="sala_asignada" name="sala_asignada" required>
                	<option value="" disabled selected>Seleccione una sala</option> <!-- Opción predeterminada -->
                    <c:forEach var="sala" items="${listaSalas}">
                        <option value="${sala.numero}">${sala.numero}</option>
                    </c:forEach>
                </select>
            </div>
            
            <!-- Grupos musculares -->
            <div class="form-group">
                <label for="grupo_muscular">Grupo Muscular:</label>
                <input type="text" id="grupo_muscular" name="grupo_muscular"  placeholder="Escribe aquí el grupo muscular" required>
            </div>
            
            <!-- Selección de monitor -->
            <div class="form-group">
                <label for="monitor">Monitor:</label>
                <select id="monitor" name="monitor" required>
                	<option value="" disabled selected>Seleccione un monitor</option> <!-- Opción predeterminada -->
                    <c:forEach var="monitor" items="${listaMonitores}">
                        <option value="${monitor.correo}">${monitor.correo}</option>
                    </c:forEach>
                </select>
            </div>
            
             <!-- Fecha límite -->
            <div class="form-group">
                <p>Fecha límite: Se crearán clases para este mes.</p>
            </div>
            
            <!-- Subir imagen -->
            <div class="form-group">
                <label for="imagen">Imagen:</label>
                <input type="file" id="imagen" name="imagen" accept="image/*" required>
            </div>
            
            <!-- Botón para enviar el formulario -->
            <button type="submit" class="submit-btn">Crear Actividad</button>
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
        </div>
        </section>
    </div>


    <!-- Script para mostrar/ocultar el panel lateral -->
    <script>
        function toggleSidebar() {
            const sidebar = document.getElementById('sidebar'); // Obtiene el panel lateral por su ID
            sidebar.classList.toggle('open'); // Alterna la clase "open" en el panel lateral
        }

		// Añadir una nueva fila de horario extra
		function addTimeRow(day) {
		    const container = document.getElementById(day + '-times');
		    const row = document.createElement('div');
		    row.classList.add('time-row');
		    
			// Construir los nombres correctamente con corchetes escapados
		    const ini = day + '_inicio[]';
		    const fin = day + '_fin[]';
		    
			console.log(ini)
			console.log(fin)

		    row.innerHTML = 
		        "<label>Inicio:</label>" +
		        "<input type='time' name='" + ini + "' onchange='updateEndTime(this)' step='3600' min='08:00' max='19:00'>" +
		        "<label>Fin:</label>" +
		        "<input type='time' name='" + fin + "' readonly>" +
		        "<button type='button' class='remove-time-btn' onclick='removeTimeRow(this)'>Eliminar</button>";

		    // Insertar el nuevo horario antes del botón "Añadir Horario"
		    container.insertBefore(row, container.querySelector('.add-time-btn'));
		}
		
		// Eliminar una fila de horario
		function removeTimeRow(button) {
		    const row = button.parentElement;
		    row.remove();
		}
        
		// Función para actualizar la hora de fin automáticamente al cambiar la hora de inicio.
		// Los minutos de la hora de inicio se modificarán a "00" si son distintos
		function updateEndTime(startTimeInput) {
		    console.log("Evento onchange activado."); // Verificar si el evento se activa

		    // Verifica que el campo de inicio tenga un valor
		    if (!startTimeInput.value) {
		        console.error("La hora de inicio no tiene un valor válido.");
		        return;
		    }

		    const timeRow = startTimeInput.closest('.time-row'); // Encuentra la fila correspondiente
		    const endTimeInput = timeRow.querySelector('input[type="time"][name$="_fin[]"]'); // Encuentra el input de hora de fin

		    if (!endTimeInput) {
		        console.error("No se encontró el campo de hora de fin en la misma fila.");
		        return;
		    }

		    let [hours, minutes] = startTimeInput.value.split(':');
		    
		    // Calcular la hora de fin como una hora después de la hora de inicio
		    let endHour = (parseInt(hours, 10) + 1) % 24; // Añade 1 y asegura que se ajuste a 24 horas

		    // Asegúrate de que los valores tengan siempre dos dígitos
		    endHour = endHour.toString().padStart(2, '0'); // Forzar a dos dígitos
		    minutes = '00'; // Forzar minutos a '00'

		    // Comprobación en la consola
		    console.log("End Hour:", endHour); // Comprobar el valor de endHour
		    console.log("Minutes:", minutes); // Comprobar el valor de minutes

		    // Asignar la hora de fin en el campo correspondiente, asegurándose de que ambos sean cadenas
		    endTimeInput.value = endHour + ':' + minutes; // Usando concatenación en lugar de plantilla de cadena
		    startTimeInput.value = hours + ':' + minutes;

		    // Comprobación en la consola
		    console.log("Hora de inicio:", startTimeInput.value);
		    console.log("Hora de fin actualizada:", endTimeInput.value);
		}
        
     	// Función para mostrar/ocultar los horarios del día seleccionado
        function toggleDayTimes(day) {
            const container = document.getElementById(day + '-times');
            const checkbox = document.getElementById(day + '-checkbox');
            container.style.display = checkbox.checked ? 'flex' : 'none';
        }
     	
     	// Validación del nombre de la imagen
        document.getElementById("imagen").addEventListener("change", function() {
            const file = this.files[0]; // Obtiene el primer archivo seleccionado
            const activityName = document.getElementById("nombre").value.trim(); // Nombre de la actividad
            const fileName = file ? file.name : ""; // Nombre del archivo, o cadena vacía si no hay archivo

            // Extraer la extensión del archivo
            const fileExtension = fileName.split('.').pop().toLowerCase(); // Obtiene la extensión y la convierte a minúsculas

            // Extraer el nombre del archivo sin la extensión
            const fileBaseName = fileName.substring(0, fileName.lastIndexOf('.'));

            // Comprueba si el nombre base del archivo coincide con el nombre de la actividad y si tiene la extensión correcta
            if (fileBaseName !== activityName || (fileExtension !== 'jpg' && fileExtension !== 'png')) {
                alert("El nombre de la imagen debe coincidir con el de la actividad y ser .jpg o .png.");
                this.value = ""; // Limpiar el campo de archivo
            }
            console.log("Nombre de la actividad:", activityName);
            console.log("Nombre del archivo:", fileBaseName);
            console.log("Extensión del archivo:", fileExtension);
        });
     	
     	
        document.querySelector('form').addEventListener('submit', function(event) {
            let atLeastOneChecked = false; // Indica si al menos un día ha sido seleccionado
            let validTimeFound = false; // Indica si se ha encontrado al menos un horario válido

            // Obtener todos los checkboxes
            const checkboxes = document.querySelectorAll('input[type="checkbox"][name="dias[]"]');

            // Verificar si al menos un checkbox está marcado
            checkboxes.forEach(checkbox => {
                if (checkbox.checked) {
                	console.log(checkbox.value) // Imprimir el día seleccionado
                    atLeastOneChecked = true; // Hay al menos un día seleccionado

                    // Comprobar si hay al menos un horario de inicio válido para este día
                    tag = checkbox.value + '_inicio[]';
                    console.log(tag)
                    tag2 = 'input[name="' + tag.replace(/\[/g, '\\[').replace(/\]/g, '\\]') + '"]'; 
                    const dayTimes = document.querySelectorAll(tag2);
                    console.log(dayTimes)
                    let timeFoundForDay = false; // Indica si se encontró un horario para el día

                    // Verificar si hay algún horario de inicio válido para este día
                    dayTimes.forEach(startTimeInput => {
                        if (startTimeInput.value) {
                            timeFoundForDay = true; // Al menos un horario de inicio es válido
                            console.log('Horario de inicio válido encontrado para ' + checkbox.value + ' : ' + startTimeInput.value);
                        }
                    });

                    // Comprobación final de horarios para el día
                    if (timeFoundForDay) {
                        validTimeFound = true; // Hay al menos un horario válido para el día
                        console.log('Se encontró al menos un horario válido para el día ' + checkbox.value);
                    } else {
                    	validTimeFound = false;
                        console.log('No se encontró un horario válido para el día ' + checkbox.value);
                    }
                }
            });

            // Validaciones
            if (!atLeastOneChecked) {
                alert("Debes marcar al menos un día."); // Mensaje si no hay días seleccionados
                console.log("Validación fallida: Ningún día seleccionado.");
                event.preventDefault(); // Prevenir el envío del formulario
                return; // Salir de la función
            }

            if (!validTimeFound) {
                alert("Debes seleccionar al menos un horario para el día marcado."); // Mensaje si no hay horarios válidos
                console.log("Validación fallida: No se encontraron horarios válidos para los días seleccionados.");
                event.preventDefault(); // Prevenir el envío del formulario
            } else {
                console.log("Validación exitosa: Todos los días seleccionados tienen horarios válidos.");
            }
        });
    </script>

</body>
</html>
