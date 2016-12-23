FROM openjdk:alpine
ADD geographicalHierarchy-0.0.1-SNAPSHOT.jar app.jar
RUN mkdir /config
RUN mkdir /logs
VOLUME ["/config", "/logs"]
CMD ["java","-jar","/app.jar" ]