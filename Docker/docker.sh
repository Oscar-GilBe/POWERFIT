#!/bin/bash
# Stop and delete the previous version if it exists
docker stop sisinf-database
docker rm sisinf-database
cd postgres
sudo docker build -t sisinf/postgresql:latest .
sudo docker run --name sisinf-database -e ALLOW_EMPTY_PASSWORD=yes -d sisinf/postgresql:latest
cd ..
# Stop and delete the previous version if it exists
docker stop sisinf-tomcat
docker rm sisinf-tomcat
cd tomcat
sudo docker build -t sisinf/tomcat:latest .
docker run -d --name sisinf-tomcat \
 --link sisinf-database \
 -p 8080:8080 \
 sisinf/tomcat:latest