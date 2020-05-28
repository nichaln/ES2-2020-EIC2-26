FROM openjdk:7 
COPY ES2-2020-EIC2-26-0.0.1-SNAPSHOT.jar /usr/src/app
WORKDIR /usr/src/myapp
CMD ["java", "-jar", "ES2-2020-EIC2-26-0.0.1-SNAPSHOT.jar"]
