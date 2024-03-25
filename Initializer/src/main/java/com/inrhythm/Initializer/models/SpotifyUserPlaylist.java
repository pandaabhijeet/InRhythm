package com.inrhythm.Initializer.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SpotifyUserPlaylist {
    private String href;
    private int limit;
    private String next;
    private int offset;
    private String previous;
    private int total;
    private List<ShowItem> items;


    @Data
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ShowItem {
        private boolean collaborative;
        private String description;
        private ExternalUrls external_urls;
        private String href;
        private String id;
        private List<Image> images;
        private String name;
        private Owner owner;
        private boolean isPublic;
        private String snapshot_id;
        private Tracks tracks;
        private String type;
        private String uri;
    }

    @Data
    @Getter
    @Setter
    public static class ExternalUrls {
        private String spotify;
    }

    @Data
    @Getter
    @Setter

    public static class Image {
        private String url;
        private int height;
        private int width;
    }

    @Data
    @Getter
    @Setter
    public static class Owner {
        private ExternalUrls external_urls;
        private Followers followers;
        private String href;
        private String id;
        private String type;
        private String uri;
        private String display_name;
    }

    @Data
    @Getter
    @Setter
    public static class Followers {
        private String href;
        private int total;
    }

    @Data
    @Getter
    @Setter
    public static class Tracks {
        private String href;
        private int total;
    }

}