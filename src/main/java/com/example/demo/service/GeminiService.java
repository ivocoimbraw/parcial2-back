package com.example.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.AngularComponentResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GeminiService {
    @Autowired
    private GeminiPromptService geminiPromptService;

    @Value("${gemini.api.key}")
    private String API_KEY;
    
    private final String BASE_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-preview-05-20:generateContent";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String getApiUrl() {
        return BASE_API_URL + "?key=" + API_KEY;
    }

    public AngularComponentResponse chatWithGemini(String userPrompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Cuerpo de la petición
        Map<String, Object> messageContent = new HashMap<>();
        Map<String, String> textPart = new HashMap<>();
        String prompt = geminiPromptService.buildPrompt(userPrompt);
        textPart.put("text", prompt);
        messageContent.put("contents", new Map[] { Map.of("parts", new Map[] { textPart }) });

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(messageContent, headers);

        // Usar el método helper para obtener la URL completa
        ResponseEntity<String> response = restTemplate.postForEntity(getApiUrl(), entity, String.class);

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            String text = root.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();

            String cleanedText = text.substring(7, text.length() - 3).trim();
            System.out.println("Response from Gemini: " + cleanedText);
            AngularComponentResponse aComponentResponse = objectMapper.readValue(cleanedText,
                    AngularComponentResponse.class);
            return aComponentResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String chatWithGemini(MultipartFile image) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Preparar el prompt
        String prompt = geminiPromptService.buildPromptImage();

        // Convertir la imagen a base64
        String base64Image = Base64.getEncoder().encodeToString(image.getBytes());

        // Estructura del cuerpo de la solicitud
        Map<String, Object> messageContent = new HashMap<>();
        List<Map<String, Object>> parts = new ArrayList<>();

        // Agregar el texto
        parts.add(Map.of("text", prompt));

        // Agregar la imagen
        Map<String, Object> inlineData = new HashMap<>();
        inlineData.put("mimeType", image.getContentType()); // Ej. "image/jpeg"
        inlineData.put("data", base64Image);
        parts.add(Map.of("inlineData", inlineData));

        // Estructura completa
        messageContent.put("contents", new Map[] { Map.of("parts", parts.toArray()) });

        // Enviar la solicitud - Usar el método helper
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(messageContent, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(getApiUrl(), entity, String.class);

        // Procesar la respuesta
        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            String text = root.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();

            return text;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}