FROM openjdk:7
WORKDIR C:/Users/nicha/.jenkins/workspace/Projeto-Jar/target/
COPY ES2-2020-EIC2-26-0.0.1-SNAPSHOT.jar /usr/src/myapp
WORKDIR /usr/src/myapp
CMD ["java", "-jar", "ES2-2020-EIC2-26-0.0.1-SNAPSHOT.jar"]
