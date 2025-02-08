<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"> <!-- Codificación de caracteres en UTF-8 -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> <!-- Hace que la página sea responsive -->
    <title>Estadísticas - Powerfit</title> <!-- Título de la página -->
    <link rel="icon" href="/POWERFIT/images/gimnasio.png"> <!-- Icono de la pestaña -->
    <link rel="stylesheet" href="/POWERFIT/css/styles.css"> <!-- Enlace al archivo CSS externo -->
    
    <style>
        .circles-container {
            display: flex; /* Usa flexbox para alinear los círculos */
            justify-content: center; /* Centra los círculos horizontalmente */
            margin-top: 120px; /* Espaciado superior */
            gap: 150px; /* Espacio entre los círculos */
            position: relative; /* Contenedor relativo para posicionar círculos de fondo */
        }
        
        .circle-wrapper {
            position: relative; /* Contenedor relativo para el círculo y su fondo */
            animation: slideUpFadeIn 1.5s ease forwards; /* Animación al cargar */
        }
        
        .circle-back {
            width: 180px; /* Tamaño del círculo de fondo */
            height: 180px;
            background-color: #FFDA6D; /* Color del círculo de fondo */
            border-radius: 50%; /* Hace que el círculo de fondo tenga bordes redondeados */
            position: absolute; /* Para colocar detrás del círculo principal */
            top: 30px; /* Desplazamiento para centrar el círculo de fondo */
            left: -10px;
            opacity: 0.4; /* Opacidad reducida para dar efecto de profundidad */
            z-index: 0; /* Posición de fondo */
        }
        
        .circle {
            width: 150px; /* Ancho del círculo */
            height: 150px; /* Altura del círculo */
            background-color: #FFDA6D; /* Color de fondo del círculo */
            border-radius: 50%; /* Hace que el círculo tenga bordes redondeados */
            display: flex; /* Usar flexbox para centrar el contenido */
            justify-content: center; /* Centrar el número horizontalmente */
            align-items: center; /* Centrar el número verticalmente */
            position: relative; /* Para posicionar el texto */
            z-index: 1; /* Para estar sobre el círculo de fondo */
        }

        .circle span {
            font-size: 28px; /* Tamaño del número */
            font-weight: bold; /* Negrita */
        }

        .title {
            text-align: center; /* Centra el texto del título */
            margin-bottom: 10px; /* Espaciado inferior */
            color: black; /* Color del texto */
            font-weight: bold; /* Negrita */
            font-size: 20px;
        }
        
        /* Animación de deslizamiento hacia arriba y desvanecimiento */
        @keyframes slideUpFadeIn {
            0% {
                transform: translateY(20px); /* Inicia desde 20px hacia abajo */
                opacity: 0; /* Comienza completamente visible */
            }
            100% {
                transform: translateY(0px); /* Termina 10px hacia arriba */
                opacity: 1; /* Se desvanece completamente */
            }
        }
    </style>
</head>
<body>

    <!-- Panel lateral -->
    <div class="sidebar" id="sidebar"> <!-- Contenedor del panel lateral -->
        <span class="close-btn" id="closeBtn" onclick="toggleSidebar()">&#10005;</span> <!-- Botón de cierre (cruz) para el panel lateral -->
        <img src="/POWERFIT/images/logo.jpg" alt="Power Fit Logo"> <!-- Imagen del logo -->
        <a href="/POWERFIT/html/administrador/home.jsp">MI CUENTA</a> <!-- Enlace a "Mi Cuenta" -->
        <a href="/POWERFIT/administrador/ListarActividades">ACTIVIDADES</a> <!-- Enlace a "Actividades" -->
        <a href="/POWERFIT/administrador/ListarMonitores">MONITORES</a> <!-- Enlace a "Monitores" -->
        <a href="/POWERFIT/administrador/ListarSalas">SALAS</a> <!-- Enlace a "Salas" -->
        <a href="/POWERFIT/html/administrador/Estadisticas" class="active">ESTADÍSTICAS</a> <!-- Enlace a "Estadísticas" -->
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

        <!-- Sección home (título y subtítulo) -->
        <div class="title-container">
            <h1 class="page-title">ESTADÍSTICAS</h1>
            <p class="page-subtitle">Aquí encontrarás las estadísticas actuales del gimnasio Power-Fit.</p>
        </div>
        
        <!-- Contenedor de círculos -->
        <div class="circles-container">
            <div class="circle-wrapper">
                <div class="circle-back"></div> <!-- Círculo de fondo detrás del círculo de clientes -->
                <p class="title">Clientes</p>
                <div class="circle">
                    <span id="clientes-count">0</span> <!-- Muestra el número de clientes -->
                </div>
            </div>
            <div class="circle-wrapper">
                <div class="circle-back"></div> <!-- Círculo de fondo detrás del círculo de monitores -->
                <p class="title">Monitores</p>
                <div class="circle">
                    <span id="monitores-count">0</span> <!-- Muestra el número de monitores -->
                </div>
            </div>
            <div class="circle-wrapper">
                <div class="circle-back"></div> <!-- Círculo de fondo detrás del círculo de actividades -->
                <p class="title">Actividades</p>
                <div class="circle">
                    <span id="actividades-count">0</span> <!-- Muestra el número de actividades -->
                </div>
            </div>
        </div>
    </div>

    <!-- Script para mostrar/ocultar el panel lateral -->
    <script>
        function toggleSidebar() {
            const sidebar = document.getElementById('sidebar'); // Obtiene el panel lateral por su ID
            sidebar.classList.toggle('open'); // Alterna la clase "open" en el panel lateral
        }
        
     	// Función para animar los contadores
        function animateValue(id, start, end, duration) {
            const obj = document.getElementById(id);
            let current = start;
            const range = end - start;
            if (end != 0) {
	            const increment = end > start ? 1 : -1;
	            const stepTime = Math.abs(Math.floor(duration / range));
	            
	            const timer = setInterval(function() {
	                current += increment;
	                obj.textContent = current;
	                if (current === end) {
	                    clearInterval(timer);
	                }
	            }, stepTime);
     		}
        }

        // Ejecuta la animación al cargar la página
        window.onload = function() {
            animateValue("clientes-count", 0, <%= request.getAttribute("nClientes") %>, 2000); // 2 segundos
            animateValue("monitores-count", 0, <%= request.getAttribute("nMonitores") %>, 2000);
            animateValue("actividades-count", 0, <%= request.getAttribute("nActividades") %>, 2000);
        };
    </script>

</body>
</html>
