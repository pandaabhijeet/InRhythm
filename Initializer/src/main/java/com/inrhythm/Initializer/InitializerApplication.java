package com.inrhythm.Initializer;
import com.inrhythm.Initializer.controller.SpotifyTokenAccessController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InitializerApplication {

	public static void main(String[] args) {
		SpringApplication.run(InitializerApplication.class, args);
		SpotifyTokenAccessController accessController = new SpotifyTokenAccessController();
	}
}
