package com.inrhythm.Initializer.controller;


import com.inrhythm.Initializer.constants.ApiPathConstants;
import com.inrhythm.Initializer.services.SpotifyCurrentUserPlaylistService;
import com.inrhythm.Initializer.services.SpotifyUserAuthService;
import com.inrhythm.Initializer.utilities.SpotifyTokenCheckUtility;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Instant;

@RestController
public class SpotifyCurrentUserPlayListController {

    public static final Logger logger = LoggerFactory.getLogger(SpotifyCurrentUserPlayListController.class);
    @Autowired
    private final SpotifyTokenCheckUtility tokenCheckUtility = new SpotifyTokenCheckUtility();

    @Autowired
    private SpotifyCurrentUserPlaylistService currentUserPlaylistService;

    @GetMapping(path = ApiPathConstants.SPOTIFY_CUR_USER_PLAYLIST, produces = MediaType.TEXT_HTML_VALUE)
    public void getCurrUserPlayList(HttpSession session, HttpServletResponse response) {

        String accessToken = (String) session.getAttribute("ACCESS_TOKEN");
        String spotifyBaseUrl = ApiPathConstants.SPOTIFY_BASE_URL + "/me/playlists";

        logger.info("Getting access token from session : " + accessToken);

        logger.info("Checking if token is null");

        if (!(accessToken == null)) {
            Instant tokenReceivedAt = (Instant) session.getAttribute("TOKEN_RECEIVED_AT");
            Long expiresIn = (Long) session.getAttribute("EXPIRES_IN");

            if (tokenCheckUtility.isExpired(tokenReceivedAt, expiresIn)) {
                logger.info("Token not null but has expired. Redirecting to refresh token");

                try {
                    response.sendRedirect(ApiPathConstants.SPOTIFY_TOKEN_REFRESH);
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }

            }

            logger.info("Initiating getCurrentUserProfile from controller");

            try {
                currentUserPlaylistService.getSpotifyUserPlayList(spotifyBaseUrl, accessToken);
            } catch (Exception exception) {
                logger.error(exception.getMessage());
                //throw new UserProfileException(exception.getMessage());
            }

        } else {
            logger.info("Spotify access token is not present. Redirecting to Auth controller");
            try {
                response.sendRedirect(ApiPathConstants.SPOTIFY_LOGIN);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            session.setAttribute("REQUEST_SOURCE", ApiPathConstants.SPOTIFY_CUR_USER_PLAYLIST);
            logger.info("waiting for access token...");
        }
    }
}
