
FROM amazoncorretto:17.0.7-alpine
WORKDIR /app
COPY target/video_chat-0.0.1-SNAPSHOT.jar ./app/video-chat.jar
EXPOSE 8080
CMD ["java", "-jar", "video-chat.jar"]
