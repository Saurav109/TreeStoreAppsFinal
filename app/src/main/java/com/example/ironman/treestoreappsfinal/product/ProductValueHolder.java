package com.example.ironman.treestoreappsfinal.product;

public class ProductValueHolder {
    public String name;
    public String decs="";
    public String imageUrl;
    public double price;
    public String seasonalad="";
    public String uses="";



    public ProductValueHolder(){ }

    public ProductValueHolder(String name, String decs, String imageUrl, double price, String seasonalad, String uses) {
        this.name = name;
        this.decs = decs;
        this.imageUrl = imageUrl;
        this.price = price;
        this.seasonalad = seasonalad;
        this.uses = uses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDecs() {
        return decs;
    }

    public void setDecs(String decs) {
        this.decs = decs;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSeasonalad() {
        return seasonalad;
    }

    public void setSeasonalad(String seasonalad) {
        this.seasonalad = seasonalad;
    }

    public String getUses() {
        return uses;
    }

    public void setUses(String uses) {
        this.uses = uses;
    }
}
