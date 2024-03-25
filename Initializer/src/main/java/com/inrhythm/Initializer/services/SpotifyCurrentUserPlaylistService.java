package com.inrhythm.Initializer.services;

import com.inrhythm.Initializer.models.SpotifyUserPlaylist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SpotifyCurrentUserPlaylistService {

    public  static  final Logger logger = LoggerFactory.getLogger(SpotifyCurrentUserPlaylistService.class);

    public SpotifyUserPlaylist getSpotifyUserPlayList(String spotifyBaseUrl, String accessToken){

        logger.info("Implementing getSpotifyUserPlayList to get playlist details.");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization" , "Bearer " + accessToken);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        logger.info("Headers set up. Starting API call to spotify.");

        try{
            ResponseEntity<SpotifyUserPlaylist> playlistResponseEntity = restTemplate.exchange(spotifyBaseUrl, HttpMethod.GET,
                    httpEntity, SpotifyUserPlaylist.class);

            if ((playlistResponseEntity.getBody() != null) && (playlistResponseEntity.getStatusCode().is2xxSuccessful())){
                logger.info("Successfully fetched user's playlist details");
                logger.info(playlistResponseEntity.getBody().getItems().get(0).getName());
                logger.info(playlistResponseEntity.getBody().getItems().get(0).getDescription());

                return playlistResponseEntity.getBody();

            }else{
                logger.info("Null response in getting playlist details");
                return  null;
            }
        }catch (RuntimeException exception){
            logger.info(exception.getMessage());
            throw  exception;
        }
    }
}
