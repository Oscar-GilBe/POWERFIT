<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="VO.UsuarioVO" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestionar Monitores - PowerFit</title>
    <link rel="icon" href="/POWERFIT/images/gimnasio.png">
    <link rel="stylesheet" href="/POWERFIT/css/styles.css">
</head>
<body>
    <!-- Panel lateral -->
    <div class="sidebar" id="sidebar">
        <span class="close-btn" id="closeBtn" onclick="toggleSidebar()">&#10005;</span>
        <img src="/POWERFIT/images/logo.jpg" alt="Power Fit Logo">
        <a href="/POWERFIT/html/administrador/home.jsp">MI CUENTA</a>
        <a href="/POWERFIT/administrador/ListarActividades">ACTIVIDADES</a>
        <a href="/POWERFIT/administrador/ListarMonitores" class="active">MONITORES</a>
        <a href="/POWERFIT/administrador/ListarSalas">SALAS</a>
        <a href="/POWERFIT/administrador/Estadisticas">ESTADÍSTICAS</a>
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
                <p class="page-subtitle">Aquí podrás dar de alta y dar de baja a los monitores que consideres.</p>
            </div>

            <!-- Botón para agregar nuevo monitor -->
            <form action="/POWERFIT/html/administrador/agregar-monitor.jsp" method="post" style="display: inline;">
                <button id="addMonitorBtn" type="submit">Dar de alta a un nuevo monitor</button>
            </form>

            <!-- Contenedor de monitores -->
            <div class="monitor-container">
                <%
                    // Obtenemos la lista de monitores del Servlet
                    List<UsuarioVO> listaMonitores = (List<UsuarioVO>) request.getAttribute("listaMonitores");

                    // Validamos que la lista no sea nula o vacía
                    if (listaMonitores != null && !listaMonitores.isEmpty()) {
                        for (UsuarioVO monitor : listaMonitores) {
                %>
                            <div class="monitor-item">
                                <div class="monitor-name-ap">
                                    <span class="monitor-name"><%= monitor.getNombre() %> <%= monitor.getApellidos() %></span>
                                </div>
                                <span class="monitor-correo"><%= monitor.getCorreo() %></span>
                                <form action="/POWERFIT/administrador/EliminarMonitor" method="post">
                                    <input type="hidden" name="correo" value="<%= monitor.getCorreo() %>">
                                    <button type="submit" class="delete-btn" onclick="return confirm('¿Estás seguro de que deseas eliminar este monitor?');">
                                        <img src="/POWERFIT/images/eliminar.png" alt="Eliminar">
                                    </button>
                                </form>
                            </div>
                <%
                        }
                    } else {
                %>
                        <p>No hay monitores disponibles para mostrar.</p>
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
            </div>
        </section>
    </div>

    <script>
        function toggleSidebar() {
            const sidebar = document.getElementById('sidebar');
            sidebar.classList.toggle('open');
        }
    </script>
</body>
</html>

