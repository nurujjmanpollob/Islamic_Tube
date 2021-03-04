package com.ibrahimhossain.app;

public class VideoData {

    String title, desc, vdourl, thumbnail;


    public VideoData(String title, String description, String videoUrl, String thumbnailUrl){


        this.title = title;
        this.desc = description;
        this.vdourl = videoUrl;
        this.thumbnail = thumbnailUrl;


    }

    public String getDescription() {
        return desc;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnailURL() {
        return thumbnail;
    }

    public String getVideoURL() {
        return vdourl;
    }

    public void setDescription(String desc) {
        this.desc = desc;
    }

    public void setThumbnailURL(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setVideoURL(String vdourl) {
        this.vdourl = vdourl;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
