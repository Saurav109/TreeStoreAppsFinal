package com.example.ironman.treestoreappsfinal.suggestion;

public class dummySuggestionValueHolder {
    public String text;
    public String imageUrl;
    public String header;

    dummySuggestionValueHolder(){}

    public dummySuggestionValueHolder(String text, String imageUrl, String header) {
        this.text = text;
        this.imageUrl = imageUrl;
        this.header = header;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
