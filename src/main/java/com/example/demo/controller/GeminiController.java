package com.example.demo.controller;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.AngularComponentResponse;
import com.example.demo.model.Prompt;
import com.example.demo.service.GeminiService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/gemini")
@SecurityRequirement(name = "bearer-key")
public class GeminiController {

    private final GeminiService geminiService;

    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/ask")
    public AngularComponentResponse askGemini(@RequestBody Prompt prompt) {
        System.out.println("Asking Gemini: " + prompt.getQuestion());
        return geminiService.chatWithGemini(prompt.getQuestion());
    }

    @GetMapping("/chat")
    public ResponseEntity<String> chatWithGemini() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("text/plain; charset=UTF-8"));

        String body = "Hello, I'm Gemini! How can I help you? ññññññ";
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    @PostMapping(value = "/chat", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> chatWithGemini(
            @RequestParam("image") MultipartFile image) throws IOException {
        String body = geminiService.chatWithGemini(image);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("text/plain; charset=UTF-8"));
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

}
