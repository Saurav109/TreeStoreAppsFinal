package com.example.ironman.treestoreappsfinal.suggestion;

public class SuggestionsValueHolder {
    public String text;
    public String imageUrl;
    public String header;
    public String parentNode="";

    public SuggestionsValueHolder(){}

    public SuggestionsValueHolder(String text, String imageUrl, String header, String parentNode) {
        this.text = text;
        this.imageUrl = imageUrl;
        this.header = header;
        this.parentNode = parentNode;
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

    public String getParentNode() {
        return parentNode;
    }

    public void setParentNode(String parentNode) {
        this.parentNode = parentNode;
    }
}
