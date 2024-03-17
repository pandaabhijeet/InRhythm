package com.inrhythm.Initializer.services;

import com.inrhythm.Initializer.models.SpotifyUserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.html.parser.Entity;

@Service
public class SpotifyUserProfileService {

    public static final Logger logger = LoggerFactory.getLogger(SpotifyUserProfileService.class);
    public SpotifyUserProfile getCurrentUserProfile(String spotifyProfileUrl, String accessToken){

        logger.info("Implementing getCurrentUserProfile to get details.");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> headerEntity = new HttpEntity<>(headers);

        logger.info("Setting up http headers. Starting API call for profile details.");
        try{
            ResponseEntity<SpotifyUserProfile> profileResponseEntity = restTemplate.exchange(spotifyProfileUrl,
                    HttpMethod.GET, headerEntity, SpotifyUserProfile.class);

            if((profileResponseEntity.getBody() != null)  && (profileResponseEntity.getStatusCode().is2xxSuccessful())){

                logger.info("Current User profile fetched successfully.");
                logger.info(profileResponseEntity.getBody().toString());
                logger.info(profileResponseEntity.getBody().getDisplay_name());
                return profileResponseEntity.getBody();

            } else {
                logger.info("Null response body or unsuccessful API call");
                return null;
            }
        }catch(Exception exception){
            logger.error(exception.getMessage());
            return null;
        }

    }
}
