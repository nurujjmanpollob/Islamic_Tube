/*
 *
 *  Copyright 2021 Nurujjaman Pollob.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */


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
