package com.example.urlshortner.projectroadmap.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.urlshortner.projectroadmap.dto.createUrl;
import com.example.urlshortner.projectroadmap.dto.updateurlrequest;
import com.example.urlshortner.projectroadmap.dto.urlresponse;
import com.example.urlshortner.projectroadmap.entity.urlshortner;
import com.example.urlshortner.projectroadmap.repository.urlrepository;
import com.example.urlshortner.projectroadmap.utils.shortcodegenerator;

@Service
public class urlshortnerservice {
    
    private final urlrepository urlRepository;

    public urlshortnerservice(urlrepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    /**
     * Creates a new shortened URL
     * @param request containing the original URL and optional shortcode
     * @return urlresponse with the created URL details
     */
    public urlresponse createShortenedUrl(createUrl request) {
        String shortcode = request.getShortcode();
        
        // Generate a shortcode if not provided
        if (shortcode == null || shortcode.isEmpty()) {
            shortcode = generateUniqueShortcode();
        } else {
            // Check if the provided shortcode already exists
            if (urlRepository.existsById(shortcode)) {
                throw new IllegalArgumentException("Shortcode already exists");
            }
        }
        
        Instant now = Instant.now();
        urlshortner urlEntity = new urlshortner(
            shortcode,
            request.getOriginalURL(),
            now,
            null,
            0
        );
        
        urlRepository.save(urlEntity);
        return mapToResponse(urlEntity);
    }

    /**
     * Retrieves URL information by shortcode
     * @param shortcode the short code to retrieve
     * @return urlresponse with the URL details
     */
    public urlresponse getUrlResponseByShortcode(String shortcode) {
        Optional<urlshortner> url = urlRepository.findById(shortcode);
        
        if (url.isPresent()) {
            urlshortner urlEntity = url.get();
            // Update last accessed time and access count
            urlEntity.setLastAccessedAt(Instant.now());
            urlEntity.setAccessCount(urlEntity.getAccessCount() + 1);
            urlRepository.save(urlEntity);
            
            return mapToResponse(urlEntity);
        }
        
        return null;
    }

    /**
     * Updates an existing shortened URL
     * @param shortcode the short code to update
     * @param request containing the new original URL
     * @return urlresponse with updated details
     */
    public urlresponse updateUrl(String shortcode, updateurlrequest request) {
        Optional<urlshortner> url = urlRepository.findById(shortcode);
        
        if (url.isPresent()) {
            urlshortner urlEntity = url.get();
            urlEntity.setOriginalURL(request.getOriginalURL());
            urlRepository.save(urlEntity);
            return mapToResponse(urlEntity);
        }
        
        return null;
    }

    /**
     * Deletes a shortened URL
     * @param shortcode the short code to delete
     * @return true if deletion was successful
     */
    public boolean deleteUrl(String shortcode) {
        if (urlRepository.existsById(shortcode)) {
            urlRepository.deleteById(shortcode);
            return true;
        }
        return false;
    }

    /**
     * Generates a unique shortcode that doesn't already exist
     * @return a unique shortcode
     */
    private String generateUniqueShortcode() {
        String shortcode;
        do {
            shortcode = shortcodegenerator.generateShortcode();
        } while (urlRepository.existsById(shortcode));
        
        return shortcode;
    }

    /**
     * Maps a URL entity to a response DTO
     * @param urlEntity the entity to map
     * @return urlresponse DTO
     */
    private urlresponse mapToResponse(urlshortner urlEntity) {
        urlresponse response = new urlresponse();
        response.setShortcode(urlEntity.getShortcode());
        response.setOriginalURL(urlEntity.getOriginalURL());
        response.setCreatedAt(urlEntity.getCreatedAt());
        response.setLastAccessedAt(urlEntity.getLastAccessedAt());
        response.setAccessCount(urlEntity.getAccessCount());
        return response;
    }
}
