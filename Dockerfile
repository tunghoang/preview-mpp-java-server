FROM maven:3.8.1-jdk-11
COPY src usr/java-server/src
COPY pom.xml usr/java-server/pom.xml
RUN mvn -f usr/java-server/pom.xml clean install
EXPOSE 8080
CMD java -jar usr/java-server/target/preview-mpp-java-server.jar
