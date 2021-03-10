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
public class AuthorProfileDetailsDatabase implements Parcelable {

    String name;
    String address;
    String avatarURL;
    String email;
    String facebooklink;
    String whatsappNo;
    String linkedinID;

    public AuthorProfileDetailsDatabase(String name, String address, String avatarURL, String emailAddress, String facebookLink, String whatsappNumber, String linkedinID){

        this.name = name;
        this.address = address;
        this.avatarURL = avatarURL;
        this.email = emailAddress;
        this.facebooklink = facebookLink;
        this.whatsappNo = whatsappNumber;
        this.linkedinID = linkedinID;
    }

    protected AuthorProfileDetailsDatabase(Parcel in) {
        name = in.readString();
        address = in.readString();
        avatarURL = in.readString();
        email = in.readString();
        facebooklink = in.readString();
        whatsappNo = in.readString();
        linkedinID = in.readString();
    }

    public static final Creator<AuthorProfileDetailsDatabase> CREATOR = new Creator<AuthorProfileDetailsDatabase>() {
        @Override
        public AuthorProfileDetailsDatabase createFromParcel(Parcel in) {
            return new AuthorProfileDetailsDatabase(in);
        }

        @Override
        public AuthorProfileDetailsDatabase[] newArray(int size) {
            return new AuthorProfileDetailsDatabase[size];
        }
    };

    public String getAddress() {
        return address;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public String getEmail() {
        return email;
    }

    public String getFacebooklink() {
        return facebooklink;
    }

    public String getLinkedinID() {
        return linkedinID;
    }

    public String getName() {
        return name;
    }

    public String getWhatsappNo() {
        return whatsappNo;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFacebooklink(String facebooklink) {
        this.facebooklink = facebooklink;
    }

    public void setLinkedinID(String linkedinID) {
        this.linkedinID = linkedinID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWhatsappNo(String whatsappNo) {
        this.whatsappNo = whatsappNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(avatarURL);
        dest.writeString(email);
        dest.writeString(facebooklink);
        dest.writeString(whatsappNo);
        dest.writeString(linkedinID);
    }
}

