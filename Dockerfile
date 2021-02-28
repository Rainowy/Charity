FROM maven:3.6-jdk-11 AS BUILD

COPY /src /app/src

COPY pom.xml /app/pom.xml

RUN mvn -f /app/pom.xml install -DskipTests

FROM openjdk:14-alpine

RUN java -version

COPY --from=BUILD /app/target/charity-*.jar /charity.jar

## Add your application to the docker image
ADD entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

## Add the wait script to the image
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.7.3/wait /wait
RUN chmod +x /wait

## Launch the wait tool and then your application
CMD /wait && /entrypoint.sh

#EXPOSE 8080

