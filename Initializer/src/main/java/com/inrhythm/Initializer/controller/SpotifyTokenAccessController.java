package com.inrhythm.Initializer.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpotifyTokenAccessController {

    @Value("${spotify.clientId}")
    private String spotifyClientId;
    @Value("${spotify.clientSecret}")
    private String spotifyClientSecret;

    @PostConstruct
    @GetMapping("/access")
    public String getSpotifyAccessToken() {
        System.out.println("Spotify Client ID: " + spotifyClientId);
        System.out.println("Spotify Client Secret: " + spotifyClientSecret);

        return "Spotify client id : "+spotifyClientId + " Spotify client secret: "+spotifyClientSecret;
    }


}


