FROM eclipse-temurin:17-jdk

WORKDIR /company-service

COPY . .

RUN chmod +x gradlew

CMD ["./gradlew", "bootRun"]