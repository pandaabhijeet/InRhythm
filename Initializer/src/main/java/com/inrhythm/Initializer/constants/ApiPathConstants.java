package com.inrhythm.Initializer.constants;

public class ApiPathConstants {

    public static final String  REDIRECT_URI = "http://localhost:8080/callback";
    public static final String  SPOTIFY_LOGIN = "/login";
    public static final String  SPOTIFY_AUTH_URL = "https://accounts.spotify.com/authorize";
    public static final String  SPOTIFY_USER_SCOPES = "user-read-private,user-read-email,user-read-playback-state,user-modify-playback-state,user-read-currently-playing,user-library-read,user-library-modify,user-read-recently-played,user-top-read,user-follow-read,user-follow-modify,playlist-read-private,playlist-modify-public,playlist-modify-private,playlist-read-collaborative,ugc-image-upload,user-read-playback-position,user-read-playback-position,user-read-private,user-read-email";
    public static final String  SPOTIFY_USER_PROFILE = "/profile";
    public static final String  SPOTIFY_BASE_URL = "https://api.spotify.com/v1";
    public static final String  SPOTIFY_TOKEN_SUCCESS = "/token-success";
    public static final String  SPOTIFY_TOKEN_ERROR = "/token-error";
    public static final String  SPOTIFY_TOKEN_REFRESH = "/token-refresh";
    public static final String  SPOTIFY_CUR_USER_PLAYLIST = "/get-cur-user-playlists";
}
