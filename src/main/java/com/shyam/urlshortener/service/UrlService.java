package com.shyam.urlshortener.service;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.shyam.urlshortener.entity.UrlMapping;
import com.shyam.urlshortener.repository.UrlMappingRepository;
import com.shyam.urlshortener.util.Base62Util;

@Service
public class UrlService {
    private final UrlMappingRepository repository;

    private static final int SHORT_CODE_LENGTH = 6;
    private static final int MAX_RETRIES = 5;
    private static final String SALT = "url-shortener-v1";

    public UrlService(UrlMappingRepository urlMappingRepository) {
        this.repository = urlMappingRepository;
    }

    public String shortenUrl(String longUrl) {
        
        UrlMapping urlMapping = new UrlMapping(longUrl);
        repository.save(urlMapping);

        Long id = urlMapping.getId();

        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            try {
                String input = id + SALT + attempt;
                String shortCode = Base62Util.encode(input, SHORT_CODE_LENGTH);

                urlMapping.setShortCode(shortCode);
                repository.save(urlMapping);

                return shortCode;
            }
            catch (DataIntegrityViolationException e) {
                // Collision occurred, retry with a different attempt number
                System.out.println("Collision detected. length=" + SHORT_CODE_LENGTH + ", attempt=" + attempt);
            }
        }
        
        throw new RuntimeException("Failed to generate unique short code after " + MAX_RETRIES + " attempts");
    }

    public Optional<String> getLongUrl(String shortCode) {
        Optional<UrlMapping> urlMappingOpt = repository.findByShortCode(shortCode);

        if(urlMappingOpt.isEmpty()) {
            return Optional.empty();
        }

        UrlMapping urlMapping = urlMappingOpt.get();
        urlMapping.setClickCount(urlMapping.getClickCount() + 1);
        repository.save(urlMapping);

        return Optional.of(urlMapping.getLongUrl());
    }
}
