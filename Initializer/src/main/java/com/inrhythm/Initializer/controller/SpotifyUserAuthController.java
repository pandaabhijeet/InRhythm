package com.inrhythm.Initializer.controller;

import com.inrhythm.Initializer.constants.ApiPathConstants;
import com.inrhythm.Initializer.services.SpotifyUserAuthService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Arrays;
import java.util.UUID;


@RestController
public class SpotifyUserAuthController {

    @Value("${spotify.clientId}")
    private String spotifyClientId;
    @Value("${spotify.clientSecret}")
    private String spotifyClientSecret;
    @Value("${spotify.tokenUrl}")
    private String spotifyTokenUrl;

    public static final Logger logger = LoggerFactory.getLogger(SpotifyUserAuthService.class);

    //SPOTIFY_LOGIN=/login
    @GetMapping(value = ApiPathConstants.SPOTIFY_LOGIN, produces = MediaType.TEXT_HTML_VALUE)
    public RedirectView redirectToSpotifyLogin(HttpSession session){

        logger.info("Redirecting to user permission for Spotify Login");

        String state = UUID.randomUUID().toString();
        session.setAttribute("SPOTIFY_STATE", state);

        String spotifyAuthUrl = ApiPathConstants.SPOTIFY_AUTH_URL +
                                "?response_type=code"+
                                "&client_id=" + spotifyClientId +
                                "&scope=" + ApiPathConstants.SPOTIFY_USER_SCOPES +
                                "&redirect_uri=" + ApiPathConstants.REDIRECT_URI +
                                "&state=" + state +
                                "&show_dialog=true";

        logger.info("SpotifyAuthUrl for this session : " + spotifyAuthUrl);

        return new RedirectView(spotifyAuthUrl);
    }

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code, @RequestParam("state") String state, HttpSession session) {

        logger.info("Handling Spotify callback now");
        String sessionState = (String) session.getAttribute("SPOTIFY_STATE");
        if (!state.equals(sessionState)) {

            logger.error("Session State mismatch : " + sessionState);
            return "redirect:/error";

        }

        logger.info("User login permission granted.");
        logger.info("Initiating token access request from Spotify.");

        SpotifyUserAuthService spotifyUserAuthService = new SpotifyUserAuthService();

        try{
            String accessToken = spotifyUserAuthService.getToken(spotifyClientId, spotifyClientSecret, spotifyTokenUrl, code);
            session.setAttribute("ACCESS_TOKEN", accessToken);
            logger.info("Access token set to http session.");
        }catch (Exception exception){

            logger.error(Arrays.toString(exception.getStackTrace()));
            throw exception;
        }

        String profileUrl = "http://localhost:8080/profile";
        return "Welcome! back to Home : " + "<a href=\"" + profileUrl + "\">" + "Home" + "</a>";
        //return "redirect:/success";
    }

    @GetMapping("/success")
    public String success() {
        return "success"; // Redirect to a success page
    }

    @GetMapping("/error")
    public String error() {
        return "error"; // Redirect to an error page
    }
}
