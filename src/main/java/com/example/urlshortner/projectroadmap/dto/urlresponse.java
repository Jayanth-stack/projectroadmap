package com.example.urlshortner.projectroadmap.dto;
import java.time.Instant;

public class urlresponse {
    
    private String shortcode;
    private String originalURL;
    private Instant createdAt;
    private Instant lastAccessedAt;
    private long accessCount;

    // Constructors
    public urlresponse() {
    }

    public urlresponse(String shortcode, String originalURL, Instant createdAt, 
                       Instant lastAccessedAt, long accessCount) {
        this.shortcode = shortcode;
        this.originalURL = originalURL;
        this.createdAt = createdAt;
        this.lastAccessedAt = lastAccessedAt;
        this.accessCount = accessCount;
    }

    // Getters
    public String getShortcode() {
        return shortcode;
    }
    
    public String getOriginalURL() {
        return originalURL;
    }
    
    public Instant getCreatedAt() {
        return createdAt;
    }
    
    public Instant getLastAccessedAt() {
        return lastAccessedAt;
    }
    
    public long getAccessCount() {
        return accessCount;
    }

    // Setters
    public void setShortcode(String shortcode) {
        this.shortcode = shortcode;
    }

    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setLastAccessedAt(Instant lastAccessedAt) {
        this.lastAccessedAt = lastAccessedAt;
    }

    public void setAccessCount(long accessCount) {
        this.accessCount = accessCount;
    }
}
