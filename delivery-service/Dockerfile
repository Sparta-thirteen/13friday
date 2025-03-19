FROM eclipse-temurin:17-jdk
WORKDIR /delivery-service

COPY . .

RUN chmod +x gradlew

CMD ["./gradlew", "bootRun"]

