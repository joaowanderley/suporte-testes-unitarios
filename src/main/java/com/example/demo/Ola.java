package com.example.demo;

public class Ola {
    private final long id;
    private final String content;

    public Ola(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
