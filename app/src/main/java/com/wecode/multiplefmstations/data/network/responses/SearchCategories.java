package com.wecode.multiplefmstations.data.network.responses;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchCategories {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("data")
    @Expose
    private List<SearchFilter> data = null;

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

    public List<SearchFilter> getData() {
        return data;
    }

    public void setData(List<SearchFilter> data) {
        this.data = data;
    }


}
