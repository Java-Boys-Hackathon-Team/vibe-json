FROM eclipse-temurin:17-jdk

COPY vibe-json-0.0.1-SNAPSHOT.jar vibe-json.jar

ENTRYPOINT ["java","-jar","/vibe-json.jar"]