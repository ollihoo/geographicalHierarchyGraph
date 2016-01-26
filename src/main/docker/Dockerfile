FROM java:8
ADD geographicalHierarchy-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
RUN mkdir /config
VOLUME /config
ENTRYPOINT ["java","-jar","/app.jar", "--neo4j.serverUrl=http://${NEO4J_PORT_7474_TCP_ADDR}:7474"]