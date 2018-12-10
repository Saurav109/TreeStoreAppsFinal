package com.example.ironman.treestoreappsfinal.blog;

public class CommentValueHolder {
    String email;
    String comment;

    CommentValueHolder(){}

    public CommentValueHolder(String comment, String email) {
        this.email = email;
        this.comment = comment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
