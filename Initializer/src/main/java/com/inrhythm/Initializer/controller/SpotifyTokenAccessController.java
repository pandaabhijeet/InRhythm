package com.inrhythm.Initializer.controller;

import com.inrhythm.Initializer.services.SpotifyTokenAccessService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpotifyTokenAccessController {

    @Value("${spotify.clientId}")
    private String spotifyClientId;
    @Value("${spotify.clientSecret}")
    private String spotifyClientSecret;

    @Value("${spotify.tokenUrl}")
    private String spotifyTokenUrl;

    @Autowired
    private SpotifyTokenAccessService spotifyTokenAccessService;

    @GetMapping("/token")
    public String getSpotifyAccessToken() {

        String token = "";

        try {
            token = spotifyTokenAccessService.getToken(spotifyClientId, spotifyClientSecret, spotifyTokenUrl);
        } catch (Exception e) {
            System.out.println(e);
        }
        return token;
    }


}


