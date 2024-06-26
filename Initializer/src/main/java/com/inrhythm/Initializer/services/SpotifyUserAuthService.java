package com.inrhythm.Initializer.services;


import com.inrhythm.Initializer.constants.ApiPathConstants;
import com.inrhythm.Initializer.exceptions.NullTokenException;
import com.inrhythm.Initializer.models.SpotifyTokenResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Objects;

@Service
public class SpotifyUserAuthService {

    public static final Logger logger = LoggerFactory.getLogger(SpotifyUserAuthService.class);

    public SpotifyTokenResponse getToken(String spotifyClientId, String spotifyClientSecret, String spotifyTokenUrl, String code) {

        logger.info("Spotify Client ID: " + spotifyClientId);

        RestTemplate restTemplate = new RestTemplate();

        //setting headers' content type
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //setting headers' authorization value
        String authCredentials = spotifyClientId + ":" + spotifyClientSecret;
        String base64Credentials = Base64.getEncoder().encodeToString(authCredentials.getBytes());
        httpHeaders.set("Authorization", "Basic " + base64Credentials);

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("grant_type", "authorization_code");
        requestParams.add("code" , code);
        requestParams.add("redirect_uri" , ApiPathConstants.REDIRECT_URI);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestParams, httpHeaders);

        try {
            logger.info("Trying to fetch Spotify Token . . .");
            ResponseEntity<SpotifyTokenResponse> spotifyTokenResponse = restTemplate.postForEntity(spotifyTokenUrl, request, SpotifyTokenResponse.class);
            System.out.println("SpotifyToken :" + Objects.requireNonNull(spotifyTokenResponse.getBody()).getAccess_token());

            logger.info(spotifyTokenResponse.getBody().toString());
            logger.info(spotifyTokenResponse.getBody().getAccess_token());
            logger.info(spotifyTokenResponse.getBody().getToken_type());
            logger.info(spotifyTokenResponse.getBody().getRefresh_token());
            logger.info(spotifyTokenResponse.getBody().getScope());
            logger.info(spotifyTokenResponse.getBody().getExpires_in().toString());

            if (spotifyTokenResponse.getBody() != null) {
                logger.info("Spotify token fetched successfully. . .");
                return spotifyTokenResponse.getBody();
            } else {
                logger.info("Null token received !");
                throw new NullTokenException("Spotify");
            }
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }

    }


    public SpotifyTokenResponse refreshToken(String spotifyTokenUrl,String spotifyClientId, String spotifyClientSecret, HttpSession session){

        RestTemplate restTemplate = new RestTemplate();

        //setting headers' content type
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String authCredentials = spotifyClientId + ":" + spotifyClientSecret;
        String base64Credentials = Base64.getEncoder().encodeToString(authCredentials.getBytes());

        logger.info("base64creds: " + base64Credentials);
        httpHeaders.set("Authorization", "Basic " + base64Credentials); //there should be space after "Basic"

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("grant_type", "refresh_token");
        requestParams.add("refresh_token", (String) session.getAttribute("REFRESH_TOKEN"));
        //requestParams.add("client_id", spotifyClientId);

        logger.info("Refresh Token : " + (String) session.getAttribute("REFRESH_TOKEN"));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestParams, httpHeaders);

        try {
            logger.info("Trying to fetch refreshed Spotify Token . . .");
            ResponseEntity<SpotifyTokenResponse> spotifyTokenResponse = restTemplate.postForEntity(spotifyTokenUrl,
                    request,SpotifyTokenResponse.class);
            System.out.println("SpotifyToken :" + Objects.requireNonNull(spotifyTokenResponse.getBody()).getAccess_token());

            logger.info(spotifyTokenResponse.getBody().toString());
            logger.info(spotifyTokenResponse.getBody().getAccess_token());
            logger.info(spotifyTokenResponse.getBody().getToken_type());
            logger.info(spotifyTokenResponse.getBody().getRefresh_token());
            logger.info(spotifyTokenResponse.getBody().getScope());
            logger.info(spotifyTokenResponse.getBody().getExpires_in().toString());

            if (spotifyTokenResponse.getBody() != null) {
                logger.info("Spotify Refresh token fetched successfully. . .");
                return spotifyTokenResponse.getBody();
            } else {
                logger.info("Null token received !");
                throw new NullTokenException("Spotify");
            }
        } catch (NullTokenException | RestClientException e) {
            logger.info(e.getMessage());
            return null;
        }

    }
}
