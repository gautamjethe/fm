package com.wecode.multiplefmstations.data.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RadioList {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("data")
    @Expose
    private List<Radio> data = null;

    public RadioList(String title, String type, List<Radio> data) {
        this.title = title;
        this.type = type;
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Radio> getData() {
        return data;
    }

    public void setData(List<Radio> data) {
        this.data = data;
    }


}