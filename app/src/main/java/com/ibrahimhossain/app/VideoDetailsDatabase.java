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

import android.os.Parcel;
import android.os.Parcelable;


@SuppressWarnings({"UnusedDeclaration"})
public class VideoDetailsDatabase implements Parcelable {

    String authorName;
    String authorAvatarURL;
    String videoTitle;
    String videoDescription;
    String videoURL;
    String videoReferenceWebsite;
    String videoThumbnail;

    //Stats
    Boolean isNotNull = true;


    //Create constructor parameter

    public VideoDetailsDatabase(String videoTitle, String videoDescription, String authorName, String authorAvatarURL, String videoURL, String videoThumbnail, String videoReferenceWebsite){

        this.videoTitle = videoTitle;
        this.videoDescription = videoDescription;
        this.authorName = authorName;
        this.authorAvatarURL = authorAvatarURL;
        this.videoURL = videoURL;
        this.videoThumbnail = videoThumbnail;
        this.videoReferenceWebsite = videoReferenceWebsite;
    }

    //Create empty constructor

    public VideoDetailsDatabase(){

        isNotNull = false;
    }


    protected VideoDetailsDatabase(Parcel in) {
        authorName = in.readString();
        authorAvatarURL = in.readString();
        videoTitle = in.readString();
        videoDescription = in.readString();
        videoURL = in.readString();
        videoReferenceWebsite = in.readString();
        videoThumbnail = in.readString();
        byte tmpIsNotNull = in.readByte();
        isNotNull = tmpIsNotNull == 0 ? null : tmpIsNotNull == 1;
    }

    public static final Creator<VideoDetailsDatabase> CREATOR = new Creator<VideoDetailsDatabase>() {
        @Override
        public VideoDetailsDatabase createFromParcel(Parcel in) {
            return new VideoDetailsDatabase(in);
        }

        @Override
        public VideoDetailsDatabase[] newArray(int size) {
            return new VideoDetailsDatabase[size];
        }
    };

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

    public String getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }

    public Boolean getIsNotNull(){

        return isNotNull;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(authorName);
        dest.writeString(authorAvatarURL);
        dest.writeString(videoTitle);
        dest.writeString(videoDescription);
        dest.writeString(videoURL);
        dest.writeString(videoReferenceWebsite);
        dest.writeString(videoThumbnail);
        dest.writeByte((byte) (isNotNull == null ? 0 : isNotNull ? 1 : 2));
    }
}
