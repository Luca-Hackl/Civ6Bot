FROM maven:3.6.3-jdk-8
WORKDIR /usr/src/app
COPY . .
RUN mvn clean package install
COPY ./config.properties ./
CMD ["java", "-jar", "target/Civ6BotMaven-1.0-SNAPSHOT.jar"]