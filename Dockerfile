#FROM openjdk:11
#
#WORKDIR /app
#
#COPY target/UserProfileManager3.jar app.jar
#
#ENV DB_USERNAME=root
##ENV DB_PASSWORD=
#ENV DB_HOST=db
#ENV DB_PORT=3307
#ENV DB_NAME=mydb
#
#EXPOSE 8083
#
#CMD ["java", "-jar", "app.jar"]

FROM openjdk:17
COPY target/UserProfileManager3-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8083
CMD ["java","-jar","app.jar"]
