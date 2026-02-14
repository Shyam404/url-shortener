package com.shyam.urlshortener.controller;

import com.shyam.urlshortener.service.UrlService;

import java.net.URI;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RedirectController {
    private final UrlService urlService;

    public RedirectController(UrlService urlService) {
        this.urlService = urlService;
    }

    @SuppressWarnings("null")
    @GetMapping("/{shortCode:[a-zA-Z0-9]{6}}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        Optional<String> longUrlOpt = urlService.getLongUrl(shortCode);

        if(longUrlOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(longUrlOpt.get())).build();
    }
}
