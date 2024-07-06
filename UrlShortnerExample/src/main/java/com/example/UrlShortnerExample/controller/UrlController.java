package com.example.UrlShortnerExample.controller;

import java.io.IOException;
import java.util.NoSuchElementException;

//
import com.example.UrlShortnerExample.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/url")
public class UrlController {

    @Autowired
    private UrlService urlService;

    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestParam String destinationUrl) {
        String shortUrl = urlService.shortenUrl(destinationUrl);
        return new ResponseEntity<>(shortUrl, HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<Boolean> updateUrl(@RequestParam String shortUrl, @RequestParam String newDestinationUrl) {
        boolean updated = urlService.updateUrl(shortUrl, newDestinationUrl);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/destination")
    public ResponseEntity<String> getDestinationUrl(@RequestParam String shortUrl) {
        String destinationUrl = urlService.getDestinationUrl(shortUrl);
        return new ResponseEntity<>(destinationUrl, HttpStatus.OK);
    }

    @PostMapping("/update-expiry")
    public ResponseEntity<Boolean> updateExpiry(@RequestParam String shortUrl, @RequestParam int daysToAdd) {
        boolean updated = urlService.updateExpiry(shortUrl, daysToAdd);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/{shortenString}")
    public void redirectToFullUrl(HttpServletResponse response, @PathVariable String shortenString) {
        try {
            String fullUrl = urlService.getDestinationUrl(shortenString);
            response.sendRedirect(fullUrl);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Url not found", e);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not redirect to the full url", e);
        }
    }
}

