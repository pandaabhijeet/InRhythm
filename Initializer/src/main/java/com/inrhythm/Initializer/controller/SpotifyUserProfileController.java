package com.inrhythm.Initializer.controller;

import com.inrhythm.Initializer.constants.ApiPathConstants;
import com.inrhythm.Initializer.exceptions.UserProfileException;
import com.inrhythm.Initializer.models.SpotifyUserProfile;
import com.inrhythm.Initializer.services.SpotifyUserAuthService;
import com.inrhythm.Initializer.services.SpotifyUserProfileService;
import com.inrhythm.Initializer.utilities.SpotifyLoginRedirectHandler;
import com.inrhythm.Initializer.utilities.SpotifyTokenCheckUtility;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class SpotifyUserProfileController {
    public static final Logger logger = LoggerFactory.getLogger(SpotifyUserAuthService.class);
    @Autowired
    private final SpotifyUserProfileService spotifyUserProfileService = new SpotifyUserProfileService();
    @Autowired
    private final SpotifyUserAuthController spotifyUserAuthController = new SpotifyUserAuthController();
    @Autowired
    private final SpotifyLoginRedirectHandler spotifyLoginRedirectHandler = new SpotifyLoginRedirectHandler();
    private SpotifyTokenCheckUtility tokenCheckUtility;

    @GetMapping(value = ApiPathConstants.SPOTIFY_USER_PROFILE, produces = MediaType.TEXT_HTML_VALUE)
    public SpotifyUserProfile getCurrentUserProfile(HttpSession session) {

        String accessToken = (String) session.getAttribute("ACCESS_TOKEN");

        logger.info("Getting access token from session : " + accessToken);
        String spotifyBaseUrl = ApiPathConstants.SPOTIFY_BASE_URL + "/me";

        /**This method will check if access token is null, if not present, it will redirect
         user to the spotify login page to get a new token.This will typically be used when
         browser was closed and session was lost.
         If the token is present, it will check for expiry, if expired, user will be redirected
         to the refresh token method to get a fresh token using the old one**/


        logger.info("Checking if token is null");

        if (!(accessToken == null)) {
            Instant tokenReceivedAt = (Instant) session.getAttribute("TOKEN_RECEIVED_AT");
            Long expiresIn = (Long) session.getAttribute("EXPIRES_IN");

            if (tokenCheckUtility.isExpired(tokenReceivedAt,expiresIn)){
                logger.info("Token not null but has expired. Redirecting to refresh token");
                spotifyUserAuthController.refreshToken(session);
            }

        } else {
            logger.info("Spotify access token is not present. Redirecting to Auth controller");
            spotifyLoginRedirectHandler.redirectToSpotifyLoginController();
        }

        logger.info("Initiating getCurrentUserProfile from controller");

        try {
            return spotifyUserProfileService.getCurrentUserProfile(spotifyBaseUrl, (String) session.getAttribute("ACCESS_TOKEN"));
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            throw new UserProfileException(exception.getMessage());
        }
    }
}


