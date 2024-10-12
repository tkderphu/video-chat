package com.example.video_chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class VideoChatApplication {


	public static void main(String[] args) {
		SpringApplication.run(VideoChatApplication.class, args);
	}

}
