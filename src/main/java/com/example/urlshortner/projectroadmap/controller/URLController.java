package com.example.urlshortner.projectroadmap.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.urlshortner.projectroadmap.dto.createUrl;
import com.example.urlshortner.projectroadmap.dto.updateurlrequest;
import com.example.urlshortner.projectroadmap.dto.urlresponse;
import com.example.urlshortner.projectroadmap.service.urlshortnerservice;

@RestController
@RequestMapping("/api/urls")
public class URLController {

    private final urlshortnerservice urlService;

    public URLController(urlshortnerservice urlService) {
        this.urlService = urlService;
    }

    /**
     * Create a new shortened URL
     * POST /api/urls
     * @param request containing originalURL and optional shortcode
     * @return ResponseEntity with the created URL details
     */
    @PostMapping
    public ResponseEntity<urlresponse> createShortenedUrl(@RequestBody createUrl request) {
        try {
            urlresponse response = urlService.createShortenedUrl(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Retrieve URL information by shortcode
     * GET /api/urls/{shortcode}
     * @param shortcode the short code
     * @return ResponseEntity with the URL details
     */
    @GetMapping("/{shortcode}")
    public ResponseEntity<urlresponse> getUrl(@PathVariable String shortcode) {
        urlresponse response = urlService.getUrlResponseByShortcode(shortcode);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Update an existing shortened URL
     * PUT /api/urls/{shortcode}
     * @param shortcode the short code to update
     * @param request containing the new originalURL
     * @return ResponseEntity with updated URL details
     */
    @PutMapping("/{shortcode}")
    public ResponseEntity<urlresponse> updateUrl(@PathVariable String shortcode, 
                                                   @RequestBody updateurlrequest request) {
        urlresponse response = urlService.updateUrl(shortcode, request);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Delete a shortened URL
     * DELETE /api/urls/{shortcode}
     * @param shortcode the short code to delete
     * @return ResponseEntity indicating success or not found
     */
    @DeleteMapping("/{shortcode}")
    public ResponseEntity<Void> deleteUrl(@PathVariable String shortcode) {
        boolean deleted = urlService.deleteUrl(shortcode);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
