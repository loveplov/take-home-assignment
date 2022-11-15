FROM openjdk:11-jre-slim

WORKDIR /
RUN mkdir /code

WORKDIR /code
COPY /target/\*.jar /code/account-transfer.jar

RUN useradd --system --user-group spring
USER spring

EXPOSE 8080
CMD ["java","-Xmx1g","-jar","/code/account-transfer.jar"]