package com.example.UrlShortnerExample.service;



import com.example.UrlShortnerExample.model.Url;
import com.example.UrlShortnerExample.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    public String shortenUrl(String destinationUrl) {
        String shortUrl = generateShortUrl();
        Url url = new Url();
        url.setShortUrl(shortUrl);
        url.setDestinationUrl(destinationUrl);
        url.setExpiryDate(LocalDateTime.now().plusMonths(10));
        urlRepository.save(url);
        return shortUrl;
    }

    public boolean updateUrl(String shortUrl, String newDestinationUrl) {
        Url url = urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new NoSuchElementException("Url not found"));
        url.setDestinationUrl(newDestinationUrl);
        urlRepository.save(url);
        return true;
    }

    public String getDestinationUrl(String shortUrl) {
        Url url = urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new NoSuchElementException("Url not found"));
        if (url.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new NoSuchElementException("Url has expired");
        }
        return url.getDestinationUrl();
    }

    public boolean updateExpiry(String shortUrl, int daysToAdd) {
        Url url = urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new NoSuchElementException("Url not found"));
        url.setExpiryDate(url.getExpiryDate().plusDays(daysToAdd));
        urlRepository.save(url);
        return true;
    }

    private String generateShortUrl() {
        return "http://localhost:8080/" + UUID.randomUUID().toString().substring(0, 8);
    }
}
