FROM eclipse-temurin:17-jdk

WORKDIR /hub-service

COPY . .

RUN chmod +x gradlew

CMD ["./gradlew", "bootRun"]