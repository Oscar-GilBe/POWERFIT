<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="VO.UsuarioVO" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestionar Monitores -  Dar de Alta - PowerFit</title>
    <link rel="icon" href="/POWERFIT/images/gimnasio.png"> <!-- Icono de la pestaña -->
    <link rel="stylesheet" href="/POWERFIT/css/styles.css"> <!-- Enlace al archivo CSS externo -->
</head>
<body>
    <!-- Panel lateral -->
    <div class="sidebar" id="sidebar">
        <span class="close-btn" id="closeBtn" onclick="toggleSidebar()">&#10005;</span>
        <img src="/POWERFIT/images/logo.jpg" alt="Power Fit Logo">
        <a href="/POWERFIT/html/administrador/home.jsp">MI CUENTA</a> <!-- Enlace a "Mi Cuenta" -->
        <a href="/POWERFIT/administrador/ListarActividades">ACTIVIDADES</a> <!-- Enlace a "Actividades" -->
        <a href="/POWERFIT/administrador/ListarMonitores" class="active">MONITORES</a> <!-- Enlace a "Monitores" -->
        <a href="/POWERFIT/administrador/ListarSalas">SALAS</a> <!-- Enlace a "Salas" -->
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
                <h1 class="page-title">GESTIONAR MONITORES</h1>
                <p class="page-subtitle">Aquí podrás dar de alta a un nuevo monitor.</p>
            </div>

            <!-- Formulario para dar de alta al monitor -->
            <form action="/POWERFIT/administrador/DarAltaMonitor" method="post" class="form-container">
                <label for="nombre">Nombre</label>
                <div class="input-field_adm">
			        <input type="text" id="nombre" name="nombre" placeholder="Escribe aquí el nombre" required>
			    </div>
                
                <label for="apellidos">Apellidos</label>
                <div class="input-field_adm">
			        <input type="text" id="apellidos" name="apellidos" placeholder="Escribe aquí los dos apellidos" required>
			    </div>
                
                <label for="email">Correo electrónico</label>
                <div class="input-field_adm">
				    <span class="ic-email"></span>
				    <input type="email" id="email" name="email" placeholder="example@powerfit.com" required>
				</div>
                
                <label for="password">Contraseña</label>
            	<div class="input-field_adm">
            	 	 <span class="ic-password"></span>
				     <input type="password" id="password" name="password" placeholder="Introduzca aquí la contraseña" required>
				     <button type="button" class="ver-password" onclick="togglePassword()">👁️</button>
				</div>
                
                <button type="submit" class="submit-btn">Añadir monitor</button>
                
                           
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
