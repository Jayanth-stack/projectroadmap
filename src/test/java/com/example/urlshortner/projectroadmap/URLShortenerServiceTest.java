package com.example.urlshortner.projectroadmap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.urlshortner.projectroadmap.dto.createUrl;
import com.example.urlshortner.projectroadmap.dto.updateurlrequest;
import com.example.urlshortner.projectroadmap.dto.urlresponse;
import com.example.urlshortner.projectroadmap.entity.urlshortner;
import com.example.urlshortner.projectroadmap.repository.urlrepository;
import com.example.urlshortner.projectroadmap.service.urlshortnerservice;

@ExtendWith(MockitoExtension.class)
public class URLShortenerServiceTest {

    @Mock
    private urlrepository urlRepository;

    @InjectMocks
    private urlshortnerservice urlService;

    private createUrl createUrlRequest;
    private updateurlrequest updateUrlRequest;
    private urlshortner urlEntity;

    @BeforeEach
    public void setUp() {
        // Initialize test data
        createUrlRequest = new createUrl();
        createUrlRequest.setOriginalURL("https://www.example.com/very-long-url");
        createUrlRequest.setShortcode("abc123");

        updateUrlRequest = new updateurlrequest();
        updateUrlRequest.setOriginalURL("https://www.example.com/updated-url");

        urlEntity = new urlshortner(
            "abc123",
            "https://www.example.com/very-long-url",
            Instant.now(),
            null,
            0
        );
    }

    @Test
    public void testCreateShortenedUrlWithProvidedShortcode() {
        when(urlRepository.existsById("abc123")).thenReturn(false);
        when(urlRepository.save(any(urlshortner.class))).thenReturn(urlEntity);

        urlresponse response = urlService.createShortenedUrl(createUrlRequest);

        assertNotNull(response);
        assertEquals("abc123", response.getShortcode());
        assertEquals("https://www.example.com/very-long-url", response.getOriginalURL());
        verify(urlRepository, times(1)).save(any(urlshortner.class));
    }

    @Test
    public void testCreateShortenedUrlWithoutShortcode() {
        createUrlRequest.setShortcode(null);
        when(urlRepository.existsById(anyString())).thenReturn(false);
        when(urlRepository.save(any(urlshortner.class))).thenReturn(urlEntity);

        urlresponse response = urlService.createShortenedUrl(createUrlRequest);

        assertNotNull(response);
        assertEquals("https://www.example.com/very-long-url", response.getOriginalURL());
        verify(urlRepository, times(1)).save(any(urlshortner.class));
    }

    @Test
    public void testCreateShortenedUrlWithDuplicateShortcode() {
        when(urlRepository.existsById("abc123")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            urlService.createShortenedUrl(createUrlRequest);
        });
    }

    @Test
    public void testGetUrlResponseByShortcode() {
        when(urlRepository.findById("abc123")).thenReturn(Optional.of(urlEntity));
        when(urlRepository.save(any(urlshortner.class))).thenReturn(urlEntity);

        urlresponse response = urlService.getUrlResponseByShortcode("abc123");

        assertNotNull(response);
        assertEquals("abc123", response.getShortcode());
        assertEquals("https://www.example.com/very-long-url", response.getOriginalURL());
        verify(urlRepository, times(1)).save(any(urlshortner.class));
    }

    @Test
    public void testGetUrlResponseByNonexistentShortcode() {
        when(urlRepository.findById("nonexistent")).thenReturn(Optional.empty());

        urlresponse response = urlService.getUrlResponseByShortcode("nonexistent");

        assertNull(response);
    }

    @Test
    public void testUpdateUrl() {
        when(urlRepository.findById("abc123")).thenReturn(Optional.of(urlEntity));
        when(urlRepository.save(any(urlshortner.class))).thenReturn(urlEntity);

        urlresponse response = urlService.updateUrl("abc123", updateUrlRequest);

        assertNotNull(response);
        assertEquals("abc123", response.getShortcode());
        verify(urlRepository, times(1)).save(any(urlshortner.class));
    }

    @Test
    public void testUpdateNonexistentUrl() {
        when(urlRepository.findById("nonexistent")).thenReturn(Optional.empty());

        urlresponse response = urlService.updateUrl("nonexistent", updateUrlRequest);

        assertNull(response);
    }

    @Test
    public void testDeleteUrl() {
        when(urlRepository.existsById("abc123")).thenReturn(true);

        boolean deleted = urlService.deleteUrl("abc123");

        assertTrue(deleted);
        verify(urlRepository, times(1)).deleteById("abc123");
    }

    @Test
    public void testDeleteNonexistentUrl() {
        when(urlRepository.existsById("nonexistent")).thenReturn(false);

        boolean deleted = urlService.deleteUrl("nonexistent");

        assertFalse(deleted);
        verify(urlRepository, never()).deleteById(anyString());
    }
}
