package com.inrhythm.Initializer.controller;

import com.inrhythm.Initializer.constants.ApiPathConstants;
import com.inrhythm.Initializer.enums.ApiPaths;
import com.inrhythm.Initializer.services.SpotifyTokenAccessService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;


@RestController
public class SpotifyUserAuthController {

    @Value("${spotify.clientId}")
    private String spotifyClientId;
    @Value("${spotify.clientSecret}")
    private String spotifyClientSecret;

    public static final Logger logger = LoggerFactory.getLogger(SpotifyTokenAccessService.class);

    //SPOTIFY_LOGIN=/login
    @GetMapping(value = ApiPathConstants.SPOTIFY_LOGIN, produces = MediaType.TEXT_HTML_VALUE)
    public RedirectView redirectToSpotifyLogin(HttpSession session){

        logger.info("Redirecting to user permission for Spotify Login");

        String state = UUID.randomUUID().toString();
        session.setAttribute("SPOTIFY_STATE", state);

        String spotifyAuthUrl = ApiPathConstants.SPOTIFY_AUTH_URL +
                                "?response_type=code"+
                                "&client_id=" + spotifyClientId +
                                "&scope=" + ApiPathConstants.SPOTIFY_SCOPE +
                                "&redirect_uri=" + ApiPathConstants.REDIRECT_URI +
                                "&state=" + state;

        logger.info("SpotifyAuthUrl for this session : " + spotifyAuthUrl);

        return new RedirectView(spotifyAuthUrl);
    }
}
