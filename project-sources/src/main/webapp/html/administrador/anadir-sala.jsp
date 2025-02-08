<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="VO.SalaVO" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Salas -  Añadir Sala - PowerFit</title>
    <link rel="icon" href="/POWERFIT/images/gimnasio.png"> <!-- Icono de la pestaña -->
    <link rel="stylesheet" href="/POWERFIT/css/styles.css"> <!-- Enlace al archivo CSS externo -->
    
    <style>
    	.input-field_adm input[type="number"] {
		    width: 100%;               /* Ocupa todo el ancho disponible */
		    padding: 10px;             /* Espaciado interno */
		    font-size: 16px;           /* Tamaño de fuente */
		    border: 1px solid #ccc;    /* Borde de color gris claro */
		    border-radius: 5px;        /* Bordes redondeados */
		    box-sizing: border-box;    /* Para que el padding no cambie el tamaño total del input */
		    -webkit-appearance: none;  /* Quita los estilos predeterminados en WebKit (Safari, Chrome) */
		    -moz-appearance: textfield; /* Quita los estilos en Firefox */
		    margin-top: 8px;				/* Margen superior */
		}
    </style>
</head>
<body>
    <!-- Panel lateral -->
    <div class="sidebar" id="sidebar">
        <span class="close-btn" id="closeBtn" onclick="toggleSidebar()">&#10005;</span>
        <img src="/POWERFIT/images/logo.jpg" alt="Power Fit Logo">
        <a href="/POWERFIT/html/administrador/home.jsp">MI CUENTA</a> <!-- Enlace a "Mi Cuenta" -->
        <a href="/POWERFIT/administrador/ListarActividades">ACTIVIDADES</a> <!-- Enlace a "Actividades" -->
        <a href="/POWERFIT/administrador/ListarMonitores">MONITORES</a> <!-- Enlace a "Monitores" -->
        <a href="/POWERFIT/administrador/ListarSalas" class="active">SALAS</a> <!-- Enlace a "Salas" -->
        <a href="/POWERFIT/administrador/Estadisticas">ESTADÍSTICAS</a> <!-- Enlace a "Estadísticas" -->
    </div>

    <!-- Contenido principal -->
    <div class="main-content">
        <div class="header">
            <span class="hamburger" onclick="toggleSidebar()">&#9776;</span>
            <div class="header-titulo">
                <span class="titulo">POWER FIT</span>
            </div>
        </div>

        <section>
            <div class="title-container">
                <h1 class="page-title">GESTIONAR SALAS</h1>
                <p class="page-subtitle">Aquí encontrarás las salas que tiene ahora mismo el gimnasio.</p>
            </div>

            <!-- Formulario para añadir la sala -->
            <form action="/POWERFIT/administrador/AnadirSala" method="post" class="form-container">
                <label for="numeroSala">Número</label>
                <div class="input-field_adm">
			        <input type="number" id="numeroSala" name="numeroSala" placeholder="Escribe aquí el número de la sala" required min="1">
			    </div>
                <button type="submit" class="submit-btn">Añadir sala</button>
                
                           
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
            </form>
        </section>
    </div>

    <script>
	    // Función para mostrar/ocultar contraseña
	    function togglePassword() {
	        var passwordField = document.getElementById('password');
	        var passwordType = passwordField.getAttribute('type');
	        
	        if (passwordType === 'password') {
	            passwordField.setAttribute('type', 'text');
	            passwordField.classList.add('show-password');
	        } else {
	            passwordField.setAttribute('type', 'password');
	            passwordField.classList.remove('show-password');
	        }
	    }
    
        function toggleSidebar() {
            const sidebar = document.querySelector('.sidebar');
            sidebar.classList.toggle('open');
        }
    </script>

</body>
</html>
