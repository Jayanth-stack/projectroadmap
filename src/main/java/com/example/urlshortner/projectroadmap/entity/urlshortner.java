package com.example.urlshortner.projectroadmap.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "urlshortner")
public class urlshortner {

    @Id
    private String shortcode;

    private String originalURL;
    private Instant createdAt;
    private Instant lastAccessedAt;
    private long accessCount;

    public urlshortner() {
    }   
    public urlshortner(String shortcode, String originalURL, Instant createdAt, Instant lastAccessedAt,
            long accessCount) {
        this.shortcode = shortcode;
        this.originalURL = originalURL;
        this.createdAt = createdAt;
        this.lastAccessedAt = lastAccessedAt;
        this.accessCount = accessCount;
    }

    // Getters and Setters
    public String getShortcode() {
        return shortcode;       
    }

    public void setShortcode(String shortcode) {
        this.shortcode = shortcode;
    }
    public String getOriginalURL() {
        return originalURL;
    }
    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
    public Instant getLastAccessedAt() {
        return lastAccessedAt;
    }
    public void setLastAccessedAt(Instant lastAccessedAt) {
        this.lastAccessedAt = lastAccessedAt;
    }
    public long getAccessCount() {
        return accessCount;
    }
    public void setAccessCount(long accessCount) {
        this.accessCount = accessCount;
    }

}
