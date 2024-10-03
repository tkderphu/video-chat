package com.example.video_chat;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class VideoChatApplication {


	public static void main(String[] args) {
		SpringApplication.run(VideoChatApplication.class, args);
	}

}
