package com.inrhythm.Initializer.controller;


import com.inrhythm.Initializer.constants.ApiPathConstants;
import com.inrhythm.Initializer.models.SpotifyUserTopTracks;
import com.inrhythm.Initializer.services.SpotifyGetUserTopTracksService;
import com.inrhythm.Initializer.utilities.SpotifyTokenCheckUtility;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Instant;

@RestController
@Controller
public class SpotifyGetUserTopTracksController {

    public final Logger logger = LoggerFactory.getLogger(SpotifyGetUserTopTracksController.class);
    @Value("${spotify.clientId}")
    private String spotifyClientId;
    @Value("${spotify.clientSecret}")
    private String spotifyClientSecret;
    @Value("${spotify.tokenUrl}")
    private String spotifyTokenUrl;
    @Autowired
    private SpotifyTokenCheckUtility tokenCheckUtility = new SpotifyTokenCheckUtility();
    @Autowired
    private SpotifyGetUserTopTracksService getUserTopTracksService = new SpotifyGetUserTopTracksService();

    public SpotifyUserTopTracks getSpotifyUserTopTracks(HttpSession session, HttpServletResponse response){

        logger.info("Initiating getSpotifyUserTopTracks from controller");

        String accessToken = (String) session.getAttribute("ACCESS_TOKEN");

        logger.info("Getting access token from session : " + accessToken);
        String spotifyBaseUrl = ApiPathConstants.SPOTIFY_BASE_URL + "/me";

        logger.info("Checking if token is null");

        if (!(accessToken == null)) {
            Instant tokenReceivedAt = (Instant) session.getAttribute("TOKEN_RECEIVED_AT");
            Long expiresIn = (Long) session.getAttribute("EXPIRES_IN");

            if (tokenCheckUtility.isExpired(tokenReceivedAt, expiresIn)) {
                logger.info("Token not null but has expired. Redirecting to refresh token");
                try{
                    response.sendRedirect(ApiPathConstants.SPOTIFY_TOKEN_REFRESH);
                }catch (IOException e){
                    logger.info(e.getMessage());
                }
            }

            logger.info("Initiating getCurrentUserProfile from controller");

            try {
                //return getUserTopTracksService
            } catch (Exception exception) {
                logger.error(exception.getMessage());
                //throw new UserProfileException(exception.getMessage());
            }

        } else {
            logger.info("Spotify access token is not present. Redirecting to Auth controller");
            try{
                response.sendRedirect(ApiPathConstants.SPOTIFY_LOGIN);
            }catch (IOException e){
                logger.info(e.getMessage());
            }
            session.setAttribute("REQUEST_SOURCE" , ApiPathConstants.SPOTIFY_USER_PROFILE);
            logger.info("waiting for access token...");
        }

        return null;

    }
}
