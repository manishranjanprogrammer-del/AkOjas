package com.ojassoft.astrosage.beans;

import java.io.Serializable;

public class PlaylistVedio implements Serializable {
    String kind;
    String etag;
    String id;
    ContentDetails contentDetails;
    SearchContentDetails searchContentDetails;
    Snippet snippet;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ContentDetails getContentDetails() {
        return contentDetails;
    }

    public void setContentDetails(ContentDetails contentDetails) {
        this.contentDetails = contentDetails;
    }

    public SearchContentDetails getSearchContentDetails() {
        return searchContentDetails;
    }

    public void setSearchContentDetails(SearchContentDetails searchContentDetails) {
        this.searchContentDetails = searchContentDetails;
    }

    public Snippet getSnippet() {
        return snippet;
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
    }

    public class ContentDetails implements Serializable{
        String videoId;
        String videoPublishedAt;

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        public String getVideoPublishedAt() {
            return videoPublishedAt;
        }

        public void setVideoPublishedAt(String videoPublishedAt) {
            this.videoPublishedAt = videoPublishedAt;
        }
    }

    public class SearchContentDetails implements Serializable{
        String videoId;

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

    }

    public class Snippet implements Serializable{
        String publishedAt;
        String channelId;
        String title;
        String description;
        String channelTitle;
        String playlistId;
        String position;
        Thumbnails thumbnails;
        ResourceId resourceId;

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getChannelTitle() {
            return channelTitle;
        }

        public void setChannelTitle(String channelTitle) {
            this.channelTitle = channelTitle;
        }

        public String getPlaylistId() {
            return playlistId;
        }

        public void setPlaylistId(String playlistId) {
            this.playlistId = playlistId;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public Thumbnails getThumbnails() {
            return thumbnails;
        }

        public void setThumbnails(Thumbnails thumbnails) {
            this.thumbnails = thumbnails;
        }

        public ResourceId getResourceId() {
            return resourceId;
        }

        public void setResourceId(ResourceId resourceId) {
            this.resourceId = resourceId;
        }

        public class Thumbnails implements Serializable{
            Default aDefault;
            Medium medium;
            High high;
            Standard standard;
            Maxres maxres;

            public Default getaDefault() {
                return aDefault;
            }

            public void setaDefault(Default aDefault) {
                this.aDefault = aDefault;
            }

            public Medium getMedium() {
                return medium;
            }

            public void setMedium(Medium medium) {
                this.medium = medium;
            }

            public High getHigh() {
                return high;
            }

            public void setHigh(High high) {
                this.high = high;
            }

            public Standard getStandard() {
                return standard;
            }

            public void setStandard(Standard standard) {
                this.standard = standard;
            }

            public Maxres getMaxres() {
                return maxres;
            }

            public void setMaxres(Maxres maxres) {
                this.maxres = maxres;
            }


            public class Default implements Serializable{
                String url;
                int width;
                int height;

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }

            public class Medium implements Serializable{
                String url;
                int width;
                int height;

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }

            public class High implements Serializable{
                String url;
                int width;
                int height;

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }

            public class Standard implements Serializable{
                String url;
                int width;
                int height;

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }

            public class Maxres implements Serializable{
                String url;
                int width;
                int height;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }


            }
        }

        public class ResourceId implements Serializable{
            String kind;
            String videoId;

            public String getKind() {
                return kind;
            }

            public void setKind(String kind) {
                this.kind = kind;
            }

            public String getVideoId() {
                return videoId;
            }

            public void setVideoId(String videoId) {
                this.videoId = videoId;
            }


        }
    }

}