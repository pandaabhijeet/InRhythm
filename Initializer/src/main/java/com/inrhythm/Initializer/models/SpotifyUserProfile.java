package com.inrhythm.Initializer.models;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Data

public class SpotifyUserProfile {
    private String country;
    private String display_name;
    private String email;
    private ExplicitContent explicit_content;
    private ExternalUrls external_urls;
    private Followers followers;
    private String href;
    private String id;
    private List<Image> images;
    private String product;
    private String type;
    private String uri;

    @Data
    public static class ExplicitContent {
        private boolean filter_enabled;
        private boolean filter_locked;
    }

    @Data
    public static class ExternalUrls {
        private String spotify;
    }

    @Data
    public static class Followers {
        private String href;
        private int total;
    }

    @Data
    public static class Image {
        private String url;
        private int height;
        private int width;
    }
}

