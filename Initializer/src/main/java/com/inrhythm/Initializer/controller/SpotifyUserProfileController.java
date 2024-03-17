package com.inrhythm.Initializer.controller;

import com.inrhythm.Initializer.constants.ApiPathConstants;
import com.inrhythm.Initializer.exceptions.UserProfileException;
import com.inrhythm.Initializer.models.SpotifyUserProfile;
import com.inrhythm.Initializer.services.SpotifyUserAuthService;
import com.inrhythm.Initializer.services.SpotifyUserProfileService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpotifyUserProfileController {
    @Autowired
    private final SpotifyUserProfileService spotifyUserProfileService = new SpotifyUserProfileService();
    public static final Logger logger = LoggerFactory.getLogger(SpotifyUserAuthService.class);

    @GetMapping(value = ApiPathConstants.SPOTIFY_USER_PROFILE, produces = MediaType.TEXT_HTML_VALUE)
    public SpotifyUserProfile getCurrentUserProfile(HttpSession session){

        String accessToken = (String) session.getAttribute("ACCESS_TOKEN");
        logger.info("Getting access token from session : " + accessToken);
        String spotifyBaseUrl = ApiPathConstants.SPOTIFY_BASE_URL + "/me";

        logger.info("Initiating getCurrentUserProfile from controller");
        try{
            return spotifyUserProfileService.getCurrentUserProfile(spotifyBaseUrl, accessToken);
        } catch (Exception exception){
            logger.error(exception.getMessage());
            throw  new UserProfileException(exception.getMessage());
        }
    }
}


