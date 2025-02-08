<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="VO.SalaVO" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"> <!-- Codificación de caracteres en UTF-8 -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> <!-- Hace que la página sea responsive -->
    <title>Salas - Powerfit</title> <!-- Título de la página -->
    <link rel="icon" href="/POWERFIT/images/gimnasio.png"> <!-- Icono de la pestaña -->
    <link rel="stylesheet" href="/POWERFIT/css/styles.css"> <!-- Enlace al archivo CSS externo -->
</head>
<body>
    <!-- Panel lateral -->
    <div class="sidebar" id="sidebar"> <!-- Contenedor del panel lateral -->
        <span class="close-btn" id="closeBtn" onclick="toggleSidebar()">&#10005;</span> <!-- Botón de cierre (cruz) para el panel lateral -->
        <img src="/POWERFIT/images/logo.jpg" alt="Power Fit Logo"> <!-- Imagen del logo -->
        <a href="/POWERFIT/html/administrador/home.jsp">MI CUENTA</a> <!-- Enlace a "Mi Cuenta" -->
        <a href="/POWERFIT/administrador/ListarActividades">ACTIVIDADES</a> <!-- Enlace a "Actividades" -->
        <a href="/POWERFIT/administrador/ListarMonitores">MONITORES</a> <!-- Enlace a "Monitores" -->
        <a href="/POWERFIT/administrador/ListarSalas" class="active">SALAS</a> <!-- Enlace a "Salas" -->
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
    
        <section>
            <!-- Encabezado de la sección -->
            <div class="title-container">
                <h1 class="page-title">GESTIONAR SALAS</h1>
                <p class="page-subtitle">Aquí encontrarás las salas que tiene ahora mismo el gimnasio.</p>
            </div>
           
            <!-- Botón para agregar nueva sala -->
            <form action="/POWERFIT/html/administrador/anadir-sala.jsp" method="post" style="display: inline;">
                <button id="addSalaBtn" type="submit">Añadir sala</button>
            </form>

            <!-- Contenedor de salas -->
            <div class="sala-container">
                <%
                    // Obtenemos la lista de salas del Servlet
                    List<SalaVO> listaSalas = (List<SalaVO>) request.getAttribute("listaSalas");

                    // Validamos que la lista no sea nula o vacía
                    if (listaSalas != null && !listaSalas.isEmpty()) {
                        for (SalaVO sala : listaSalas) {
                %>
                            <div class="sala-item">
                                <div class="sala-name-ap">
                                    <span class="sala-name">Sala número <%= sala.getNumero() %></span>
                                </div>
                                <form action="/POWERFIT/administrador/EliminarSala" method="post">
                                    <input type="hidden" name="numeroSala" value="<%= sala.getNumero() %>">
                                    <button type="submit" class="delete-btn" onclick="return confirm('¿Estás seguro de que deseas eliminar esta sala?');">
                                        <img src="/POWERFIT/images/eliminar.png" alt="Eliminar">
                                    </button>
                                </form>
                            </div>
                <%
                        }
                    } else {
                %>
                        <p>No hay salas disponibles para mostrar.</p>
                <%
                    }
                %>
                
                <!-- Mensaje de error si lo hubiera -->
                <%
                    String error = (String) request.getSession().getAttribute("error");
                    if (error != null && !error.isEmpty()) {
                %>
                        <div class="error">
                            <p><%= error %></p>
                        </div>
                <%
                        // Remover el mensaje de error de la sesión
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
