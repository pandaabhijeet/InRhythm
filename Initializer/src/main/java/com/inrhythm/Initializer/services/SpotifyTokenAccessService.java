package com.inrhythm.Initializer.services;


import com.inrhythm.Initializer.models.SpotifyTokenResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Objects;

@Service
public class SpotifyTokenAccessService {
    public String getToken(String spotifyClientId, String spotifyClientSecret, String spotifyTokenUrl) {
        System.out.println("Spotify Client ID: " + spotifyClientId);
        System.out.println("Spotify Client Secret: " + spotifyClientSecret);
        System.out.println("Spotify Client Secret: " + spotifyTokenUrl);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String authCredentials = spotifyClientId + ":" + spotifyClientSecret;
        String base64Credentials = Base64.getEncoder().encodeToString(authCredentials.getBytes());

        httpHeaders.set("Authorization", "Basic " + base64Credentials );

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestParams,httpHeaders);

        try {
            ResponseEntity<SpotifyTokenResponse>  spotifyTokenResponse = restTemplate.postForEntity(spotifyTokenUrl,request, SpotifyTokenResponse.class);
            System.out.println("SpotifyToken :" + Objects.requireNonNull(spotifyTokenResponse.getBody()).getAccess_token());

            return spotifyTokenResponse.getBody().getAccess_token();
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }

    }
}
