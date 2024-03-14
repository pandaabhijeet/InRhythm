package com.inrhythm.Initializer;
import com.inrhythm.Initializer.controller.SpotifyTokenAccessController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class InitializerApplication {

	public static final Logger logger = LoggerFactory.getLogger(InitializerApplication.class);
	public static void main(String[] args) {
 		SpringApplication.run(InitializerApplication.class, args);
		//SpotifyTokenAccessController accessController = new SpotifyTokenAccessController();

		logger.info("I am starting now !!!");
	}

	@GetMapping("/")
	public String home(){
		String loginUrl = "http://localhost:8080/login";
		return "Welcome! Login to Spotify : " + "<a href=\"" + loginUrl + "\">" + "Spotify" + "</a>";
	}

	@GetMapping("/login")
	public String login(){
		return "Welcome! Login to Spotify : ";
	}
}
