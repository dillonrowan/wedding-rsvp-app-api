FROM azul/zulu-openjdk:17 AS build
ENV APP_HOME=/usr/app
WORKDIR $APP_HOME
COPY . .
RUN ./gradlew bootJar

FROM azul/zulu-openjdk:17 AS run
ENV APP_HOME=/usr/app
WORKDIR $APP_HOME
COPY --from=build $APP_HOME/build/libs/wedding-rsvp-app-api.jar .

USER nobody
EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70", "-jar", "wedding-rsvp-app-api.jar"]
