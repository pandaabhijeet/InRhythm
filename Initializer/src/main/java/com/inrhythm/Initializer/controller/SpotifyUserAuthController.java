package com.inrhythm.Initializer.controller;

import com.inrhythm.Initializer.constants.ApiPathConstants;
import com.inrhythm.Initializer.models.SpotifyTokenResponse;
import com.inrhythm.Initializer.services.SpotifyUserAuthService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;


@RestController
public class SpotifyUserAuthController {

    public final Logger logger = LoggerFactory.getLogger(SpotifyUserAuthController.class);
    @Value("${spotify.clientId}")
    private String spotifyClientId;
    @Value("${spotify.clientSecret}")
    private String spotifyClientSecret;
    @Value("${spotify.tokenUrl}")
    private String spotifyTokenUrl;

    //SPOTIFY_LOGIN=/login
    @Async
    @GetMapping(value = ApiPathConstants.SPOTIFY_LOGIN, produces = MediaType.TEXT_HTML_VALUE)
    public RedirectView redirectToSpotifyLogin(HttpSession session) {

        logger.info("Redirecting to user permission for Spotify Login");

        String state = UUID.randomUUID().toString();
        session.setAttribute("SPOTIFY_STATE", state);

        String spotifyAuthUrl = ApiPathConstants.SPOTIFY_AUTH_URL +
                "?response_type=code" +
                "&client_id=" + spotifyClientId +
                "&scope=" + ApiPathConstants.SPOTIFY_USER_SCOPES +
                "&redirect_uri=" + ApiPathConstants.REDIRECT_URI +
                "&state=" + state;
        //+"&show_dialog=true";

        logger.info("SpotifyAuthUrl for this session : " + spotifyAuthUrl);

        return new RedirectView(spotifyAuthUrl);
    }

    @Async
    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code, @RequestParam("state") String state, HttpSession session,
                           HttpServletResponse response) throws IOException {

        logger.info("Handling Spotify callback now");
        String sessionState = (String) session.getAttribute("SPOTIFY_STATE");
        if (!state.equals(sessionState)) {

            logger.error("Session State mismatch : " + sessionState);
            response.sendRedirect("http://localhost:8080/token-error");

        }

        logger.info("User login permission granted.");
        logger.info("Initiating token access request from Spotify.");

        SpotifyUserAuthService spotifyUserAuthService = new SpotifyUserAuthService();

        try {
            SpotifyTokenResponse spotifyTokenResponse = spotifyUserAuthService.getToken(spotifyClientId, spotifyClientSecret, spotifyTokenUrl, code);

            session.setAttribute("ACCESS_TOKEN", spotifyTokenResponse.getAccess_token());
            logger.info("Access token set to http session.");

            session.setAttribute("TOKEN_RECEIVED_AT", Instant.now());
            logger.info("Token received instant set to http session.");

            //session.setAttribute("EXPIRES_IN", spotifyTokenResponse.getExpires_in());
            Integer exp = 90;
            session.setAttribute("EXPIRES_IN", exp.longValue());
            logger.info("Token expiration time set to http session.");

            session.setAttribute("REFRESH_TOKEN", spotifyTokenResponse.getRefresh_token());
            logger.info("Refresh token set to http session.");

        } catch (Exception exception) {

            logger.error(Arrays.toString(exception.getStackTrace()));
            throw exception;
        }

        //String profileUrl = "http://localhost:8080/profile";
        //return "Welcome! back to Home : " + "<a href=\"" + profileUrl + "\">" + "Home" + "</a>";

        response.sendRedirect(ApiPathConstants.SPOTIFY_TOKEN_SUCCESS);
        return "redirect:/success";
    }


    @Async
    @GetMapping("/token-refresh")
    public String refreshToken(HttpSession session, HttpServletResponse response) throws IOException {
        logger.info("Initiating a token refresh request from Spotify.");
        SpotifyUserAuthService spotifyUserAuthService = new SpotifyUserAuthService();

        try {
            SpotifyTokenResponse spotifyTokenResponse = spotifyUserAuthService.refreshToken(spotifyTokenUrl,spotifyClientId,spotifyClientSecret,session);

            session.setAttribute("ACCESS_TOKEN", spotifyTokenResponse.getAccess_token());
            logger.info("Access token set to http session.");

            session.setAttribute("TOKEN_RECEIVED_AT", Instant.now());
            logger.info("Token received instant set to http session.");

            //session.setAttribute("EXPIRES_IN", spotifyTokenResponse.getExpires_in());
            Integer exp = 90;
            session.setAttribute("EXPIRES_IN", exp.longValue());
            logger.info("Token expiration time set to http session.");

            session.setAttribute("REFRESH_TOKEN", spotifyTokenResponse.getRefresh_token());
            logger.info("Refresh token set to http session.");

            if (spotifyTokenResponse.getAccess_token() != null) {
                response.sendRedirect(ApiPathConstants.SPOTIFY_TOKEN_SUCCESS);
            } else {
                response.sendRedirect(ApiPathConstants.SPOTIFY_TOKEN_ERROR);
            }

            return "redirect:/success";

        } catch (Exception exception) {
            logger.error(Arrays.toString(exception.getStackTrace()));
            return "redirect:/error";
        }
    }

    @GetMapping("/token-success")
    public String success(HttpSession session, HttpServletResponse response) throws IOException {
        logger.info("Redirecting to the source page of token request.");
        response.sendRedirect((String) session.getAttribute("REQUEST_SOURCE"));

        return "success";
    }

    @GetMapping("/token-error")
    public String error() {

        logger.info("Redirecting to the error page.");
        return "error"; // Redirect to an error page
    }


}
