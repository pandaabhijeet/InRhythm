package com.inrhythm.Initializer.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SpotifyTokenAccessController {

    @Value("${spotify.clientId}")
    private String spotifyClientId;
    @Value("${spotify.clientSecret}")
    private String spotifyClientSecret;

    @PostConstruct
    public void init() {
        System.out.println("Spotify Client ID: " + spotifyClientId);
        System.out.println("Spotify Client Secret: " + spotifyClientSecret);
    }
}


