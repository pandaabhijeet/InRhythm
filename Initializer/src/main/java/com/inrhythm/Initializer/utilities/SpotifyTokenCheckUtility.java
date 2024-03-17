package com.inrhythm.Initializer.utilities;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class SpotifyTokenCheckUtility {

    public boolean isExpired(Instant receivedAt , Long expiresIn){
        Instant now = Instant.now();
        Instant expiration = receivedAt.plusSeconds(expiresIn);

        return now.isAfter(expiration);
    }
}
