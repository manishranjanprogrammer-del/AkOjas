package com.ojassoft.astrosage.beans;

import java.io.Serializable;

/**
 * Created by ojas-08 on 5/7/17.
 */
public class YoutubeData implements Serializable {
    private String kind;
    String eteg;
    objectInfo objectInfo;
    Snippet snippet;


    public YoutubeData.objectInfo getObjectInfo() {
        return objectInfo;
    }

    public void setObjectInfo(YoutubeData.objectInfo objectInfo) {
        this.objectInfo = objectInfo;
    }

    public Snippet getSnippet() {
        return snippet;
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
    }


    public String getEteg() {
        return eteg;
    }

    public void setEteg(String eteg) {
        this.eteg = eteg;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

   public  class objectInfo implements  Serializable{
        String kind;
        String vedioId;

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getVedioId() {
            return vedioId;
        }

        public void setVedioId(String vedioId) {
            this.vedioId = vedioId;
        }


    }

    public class Snippet implements  Serializable{
        String publishedAt;
        String channelId;
        String title;
        String description;
        Thumbnail thumbnail;
        ResourceId resourceId;

        public Thumbnail getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(Thumbnail thumbnail) {
            this.thumbnail = thumbnail;
        }


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

        public ResourceId getResourceId() {
            return resourceId;
        }

        public void setResourceId(ResourceId resourceId) {
            this.resourceId = resourceId;
        }


        public class Thumbnail implements  Serializable{
            Tdefault tdefault;
            Thigh thigh;
            Tmedium tmedium;

            public Tdefault getTdefault() {
                return tdefault;
            }

            public void setTdefault(Tdefault tdefault) {
                this.tdefault = tdefault;
            }

            public Thigh getThigh() {
                return thigh;
            }

            public void setThigh(Thigh thigh) {
                this.thigh = thigh;
            }

            public Tmedium getTmedium() {
                return tmedium;
            }

            public void setTmedium(Tmedium tmedium) {
                this.tmedium = tmedium;
            }


           public  class Tdefault implements  Serializable {
                String url;
                String width;
                String height;

                public String getWidth() {
                    return width;
                }

                public void setWidth(String width) {
                    this.width = width;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getHeight() {
                    return height;
                }

                public void setHeight(String height) {
                    this.height = height;
                }
            }

            public class Tmedium implements  Serializable  {
                String url;
                String width;
                String height;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getWidth() {
                    return width;
                }

                public void setWidth(String width) {
                    this.width = width;
                }

                public String getHeight() {
                    return height;
                }

                public void setHeight(String height) {
                    this.height = height;
                }


            }

            public class Thigh implements  Serializable {

                String url;
                String width;
                String height;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getHeight() {
                    return height;
                }

                public void setHeight(String height) {
                    this.height = height;
                }

                public String getWidth() {
                    return width;
                }

                public void setWidth(String width) {
                    this.width = width;
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
