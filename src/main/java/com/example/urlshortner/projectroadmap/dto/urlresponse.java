package com.example.urlshortner.projectroadmap.dto;
import java.time.Instant;

public class urlresponse {
    
    private String shortcode;
    private String originalURL;
    private Instant createdAt;
    private Instant lastAccessedAt;
    private long accessCount;

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
    

}
