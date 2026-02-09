package com.shyam.urlshortener.controller;

import org.springframework.web.bind.annotation.*;
import com.shyam.urlshortener.service.UrlService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public String shorten(@RequestParam String longUrl, HttpServletRequest request) {
        String code = urlService.shortenUrl(longUrl);
        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        
        return baseUrl + "/" + code;
    }
}
