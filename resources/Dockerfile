FROM adoptopenjdk:11-jre-hotspot

COPY build/libs/ktorwebapp-0.0.1.jar /opt/ktorwebapp/ktorwebapp.jar

EXPOSE 8080

CMD ["java", "-jar", "/opt/ktorwebapp/ktorwebapp.jar"]
