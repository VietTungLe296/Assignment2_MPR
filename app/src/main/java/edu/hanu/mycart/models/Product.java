package edu.hanu.mycart.models;


import android.graphics.Bitmap;

public class Product {
    private Integer id;
    private String thumbnail;
    private String name;
    private Integer unitPrice;
    private Bitmap image;

    public Product(Integer id, String thumbnail, String name, Integer unitPrice) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.unitPrice = unitPrice;
    }

    public Product() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }


    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
