
package com.example.dell.instagramlogin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelInstaProfile {

    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public class Meta {

        @SerializedName("code")
        @Expose
        private Integer code;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

    }

    public class Data {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("profile_picture")
        @Expose
        private String profilePicture;
        @SerializedName("full_name")
        @Expose
        private String fullName;
        @SerializedName("bio")
        @Expose
        private String bio;
        @SerializedName("website")
        @Expose
        private String website;
        @SerializedName("is_business")
        @Expose
        private Boolean isBusiness;
        @SerializedName("counts")
        @Expose
        private Counts counts;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getProfilePicture() {
            return profilePicture;
        }

        public void setProfilePicture(String profilePicture) {
            this.profilePicture = profilePicture;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public Boolean getIsBusiness() {
            return isBusiness;
        }

        public void setIsBusiness(Boolean isBusiness) {
            this.isBusiness = isBusiness;
        }

        public Counts getCounts() {
            return counts;
        }

        public void setCounts(Counts counts) {
            this.counts = counts;
        }

    }

    public class Counts {

        @SerializedName("media")
        @Expose
        private Integer media;
        @SerializedName("follows")
        @Expose
        private Integer follows;
        @SerializedName("followed_by")
        @Expose
        private Integer followedBy;

        public Integer getMedia() {
            return media;
        }

        public void setMedia(Integer media) {
            this.media = media;
        }

        public Integer getFollows() {
            return follows;
        }

        public void setFollows(Integer follows) {
            this.follows = follows;
        }

        public Integer getFollowedBy() {
            return followedBy;
        }

        public void setFollowedBy(Integer followedBy) {
            this.followedBy = followedBy;
        }

    }
}
