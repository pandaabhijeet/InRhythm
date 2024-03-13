package com.inrhythm.Initializer.services;


import com.inrhythm.Initializer.exceptions.NullTokenException;
import com.inrhythm.Initializer.models.SpotifyTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public static final Logger logger = LoggerFactory.getLogger(SpotifyTokenAccessService.class);

    public String getToken(String spotifyClientId, String spotifyClientSecret, String spotifyTokenUrl) {
        System.out.println("Spotify Client ID: " + spotifyClientId);
        System.out.println("Spotify Client Secret: " + spotifyClientSecret);
        System.out.println("Spotify Client Secret: " + spotifyTokenUrl);

        logger.info("Spotify Client ID: " + spotifyClientId);


        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String authCredentials = spotifyClientId + ":" + spotifyClientSecret;
        String base64Credentials = Base64.getEncoder().encodeToString(authCredentials.getBytes());

        httpHeaders.set("Authorization", "Basic " + base64Credentials);

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestParams, httpHeaders);

        try {
            logger.info("Trying to fetch Spotify Token . . .");
            ResponseEntity<SpotifyTokenResponse> spotifyTokenResponse = restTemplate.postForEntity(spotifyTokenUrl, request, SpotifyTokenResponse.class);
            System.out.println("SpotifyToken :" + Objects.requireNonNull(spotifyTokenResponse.getBody()).getAccess_token());

            if (spotifyTokenResponse.getBody().getAccess_token() != null) {
                logger.info("Spotify token fetched successfully. . .");
                return spotifyTokenResponse.getBody().getAccess_token();
            } else {
                logger.info("Null token received !");
                throw new NullTokenException("Spotify");
            }
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }

    }
}
