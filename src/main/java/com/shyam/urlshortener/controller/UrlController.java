package com.shyam.urlshortener.controller;

import org.springframework.web.bind.annotation.*;
import com.shyam.urlshortener.service.UrlService;

@RestController
@RequestMapping("/api")
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public String shorten(@RequestParam String longUrl) {
        String code = urlService.shortenUrl(longUrl);
        return "http://localhost:8080/" + code;
    }
}
