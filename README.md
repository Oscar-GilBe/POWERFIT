# POWERFIT

**POWERFIT** es un sistema de información diseñado para gestionar y reservar actividades en el gimnasio. La aplicación está pensada para tres tipos de usuarios:

- **Cliente**
- **Monitor**
- **Administrador**

Cada rol tiene funcionalidades específicas que se detallan a continuación.

---

# Funcionalidades del Sistema

## Cliente

- **Registro y Autenticación**
  - El sistema permitirá registrarse aportando su información personal: **nombre**, **apellidos**, **correo** y **contraseña**.
  - El sistema permitirá identificarse introduciendo su **correo** y **contraseña**.
  - El sistema permitirá cerrar la sesión.
  
- **Gestión de Cuenta**
  - El sistema permitirá eliminar permanentemente la cuenta.
  - El sistema permitirá modificar su información personal (nombre, apellidos y contraseña).

- **Actividades**
  - El sistema permitirá listar las actividades ofertadas por el gimnasio.
  - El sistema permitirá consultar la información de las actividades, que incluye:
    - Nombre
    - Número total de plazas ofertadas
    - Horarios
    - Nivel de dificultad
    - Material necesario
    - Sala asignada
    - Grupo(s) muscular(es) a trabajar
    - Puntuación media
    - Reseñas de clientes
    - Imagen
  - El sistema permitirá filtrar las actividades por diferentes criterios: horario, grupo muscular, nivel de dificultad, puntuación y sala donde se imparte.

- **Reservas y Reseñas**
  - El sistema permitirá reservar una plaza en una determinada clase, si aún quedan plazas disponibles.
  - El sistema permitirá cancelar una reserva realizada para una clase.
  - El sistema permitirá consultar un historial de las clases reservadas.
  - El sistema permitirá puntuar las actividades con una escala de una a cinco estrellas y mediante reseñas escritas.
  - El sistema permitirá modificar sus reseñas y puntuaciones previamente realizadas.
  - El sistema permitirá eliminar sus reseñas y puntuaciones previamente realizadas.

---

## Monitor

- **Autenticación y Sesión**
  - El sistema permitirá identificarse introduciendo su correo y contraseña.
  - El sistema permitirá cerrar la sesión.

- **Gestión de Perfil**
  - El sistema permitirá modificar su información personal (nombre, apellidos y contraseña).

- **Clases**
  - El sistema permitirá visualizar las clases que tiene encargadas.

---

## Administrador

- **Autenticación**
  - El sistema permitirá identificarse introduciendo su correo y contraseña.

- **Gestión de Monitores**
  - El sistema permitirá dar de alta a monitores, a partir de su nombre y apellidos, y asignándole su correo corporativo y contraseña.
  - El sistema permitirá dar de baja a un monitor.

- **Gestión de Actividades**
  - El sistema permitirá ofertar nuevas actividades, añadiendo:
    - Nombre
    - Número total de plazas ofertadas
    - Horarios (días y horas)
    - Nivel de dificultad
    - Material necesario
    - Sala asignada
    - Grupo(s) muscular(es) a trabajar
    - Monitor asignado
    - Imagen (obligatorio)
  - El sistema permitirá eliminar una actividad.
  - El sistema permitirá modificar la información de una actividad:
    - Número total de plazas ofertadas
    - Horarios del siguiente mes (días y horas)
    - Nivel de dificultad
    - Material necesario
    - Sala asignada
    - Grupo(s) muscular(es) a trabajar
    - Monitor asignado
    - Imagen (opcional)

- **Gestión de Salas**
  - El sistema permitirá añadir salas especificando su número.
  - El sistema permitirá eliminar salas.
  
- **Estadísticas del Gimnasio**
  - El sistema permitirá consultar las estadísticas del gimnasio: número de clientes, número de monitores y número de actividades ofertadas.
---

## Cuestiones Necesarias para el Uso de la Aplicación en Docker

Para acceder a la página inicial de la aplicación, se debe escribir en el navegador la siguiente dirección:
`localhost:8080`

### Cuentas de Usuario para Pruebas en Docker

Para probar nuestro sistema, hemos almacenado en la base de datos las siguientes cuentas de usuario, las cuales pertenecen a distintos roles y permiten acceder a diferentes pantallas:

#### ADMINISTRADOR
- **Correo:** `admin@powerfit.com`  
  **Contraseña:** `admin`

#### MONITORES
Las cuentas de monitores tienen una contraseña con el formato `password_{nombre}`.  
Ejemplos:
- **Correo:** `laura.s@powerfit.com`  
  **Contraseña:** `password_laura`
- **Correo:** `paula.p@powerfit.com`  
  **Contraseña:** `password_paula`
- *Análogamente para las cuentas del resto de monitores.*

> **Nota:** Se pueden eliminar cuentas de monitores existentes y crear una nueva cuenta siempre que el correo del monitor finalice con el sufijo `@powerfit.com`.

#### CLIENTES
Ejemplos de cuentas de clientes:
- **Correo:** `sandra.f@hotmail.com`  
  **Contraseña:** `password_sandra`
- **Correo:** `hector.p@unizar.es`  
  **Contraseña:** `password_hector`
- **Correo:** `laura.m@gmail.com`  
  **Contraseña:** `password_laura`
- **Correo:** `andres.c@hotmail.com`  
  **Contraseña:** `password_andres`
- *Análogamente para las cuentas del resto de clientes.*

> **Nota:** Se pueden eliminar cuentas de clientes existentes y crear una nueva cuenta con el sufijo que el cliente desee.

