FROM bitnami/postgresql:17
# Mantenedor
LABEL authors="873680, 879128, 870919"
# Crear el directorio faltante y actualizar el sistema, instalar locales y configurar es_ES.UTF-8 con permisos de superusuario
USER root
RUN apt-get update && apt-get install -y locales apt-utils \
&& echo "es_ES.UTF-8 UTF-8" > /etc/locale.gen \
&& locale-gen \
&& update-locale LANG=es_ES.UTF-8 LC_ALL=es_ES.UTF-8 LANGUAGE=es_ES.UTF-8 \
&& apt-get clean
# Establecer las variables de entorno de locales
ENV LANG=es_ES.UTF-8
ENV LANGUAGE=es_ES:es
ENV LC_ALL=es_ES.UTF-8
# Copiar los datos
ADD --chown=1001:1001 main /bitnami/postgresql/data