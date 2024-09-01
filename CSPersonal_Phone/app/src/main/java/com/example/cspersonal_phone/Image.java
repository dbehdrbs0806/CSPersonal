package com.example.cspersonal_phone;

import com.google.gson.annotations.SerializedName;

// image 객체로 json으로 사용할 Gson 객체파일
public class Image {

    @SerializedName("name")   // json key로 저장될 값
    private String name;

    @SerializedName("image")
    private String imageBase64;

    @SerializedName("width")
    private int width;

    @SerializedName("height")
    private int height;

    public Image(String name, String imageBase64, int width, int height) {
        this.name = name;
        this.imageBase64 = imageBase64;
        this.width = width;
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
