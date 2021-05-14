package com.wecode.multiplefmstations.data.network.responses;

import com.google.gson.annotations.SerializedName;

public class FeedbackBody {
    @SerializedName("title")
    private String title;
    @SerializedName("email")
    private String email;
    @SerializedName("description")
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
