package com.wecode.multiplefmstations.data.network.responses;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "favorite_table")
public class Radio implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int fid;

    @ColumnInfo(name = "radio_id")
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("genres")
    @Expose
    private String genres;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("streaming_link")
    @Expose
    private String streamingLink;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("type")
    @Expose
    private String type;


    public Radio(Integer id, String country, String category, String genres, String language, String name, String title, String image, String streamingLink, Integer count, String description, String type) {
        this.id = id;
        this.country = country;
        this.category = category;
        this.genres = genres;
        this.language = language;
        this.name = name;
        this.title = title;
        this.image = image;
        this.streamingLink = streamingLink;
        this.count = count;
        this.description = description;
        this.type = type;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStreamingLink() {
        return streamingLink;
    }

    public void setStreamingLink(String streamingLink) {
        this.streamingLink = streamingLink;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

