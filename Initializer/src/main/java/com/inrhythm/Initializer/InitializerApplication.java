package com.inrhythm.Initializer;
import com.inrhythm.Initializer.controller.SpotifyTokenAccessController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InitializerApplication {

	public static final Logger logger = LoggerFactory.getLogger(InitializerApplication.class);
	public static void main(String[] args) {
 		SpringApplication.run(InitializerApplication.class, args);
		//SpotifyTokenAccessController accessController = new SpotifyTokenAccessController();

		logger.info("I am starting now !!!");
	}
}
