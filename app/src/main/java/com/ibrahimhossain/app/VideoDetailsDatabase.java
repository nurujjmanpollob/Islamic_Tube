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

public class VideoDetailsDatabase {

    String authorName;
    String authorAvatarURL;
    String videoTitle;
    String videoDescription;
    String videoURL;
    String videoReferenceWebsite;


    //Create constructor parameter
    public VideoDetailsDatabase(String videoTitle, String videoDescription, String authorName, String authorAvatarURL, String videoURL, String videoReferenceWebsite){

        this.videoTitle = videoTitle;
        this.videoDescription = videoDescription;
        this.authorName = authorName;
        this.authorAvatarURL = authorAvatarURL;
        this.videoURL = videoURL;
        this.videoReferenceWebsite = videoReferenceWebsite;
    }


    public void setAuthorAvatarURL(String authorAvatarURL) {
        this.authorAvatarURL = authorAvatarURL;
    }

    public String getAuthorAvatarURL() {
        return authorAvatarURL;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideoReferenceWebsite(String videoReferenceWebsite) {
        this.videoReferenceWebsite = videoReferenceWebsite;
    }

    public String getVideoReferenceWebsite() {
        return videoReferenceWebsite;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }


}
