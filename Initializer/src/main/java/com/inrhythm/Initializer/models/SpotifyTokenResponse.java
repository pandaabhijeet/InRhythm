package com.inrhythm.Initializer.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SpotifyTokenResponse {

    @JsonProperty("access_token")
    private String access_token;
    @JsonProperty("token_type")
    private String token_type;
    @JsonProperty("expires_in")
    private Long expires_in;
    @JsonProperty("refresh_token")
    private String refresh_token;
    @JsonProperty("scope")
    private String scope;

}
