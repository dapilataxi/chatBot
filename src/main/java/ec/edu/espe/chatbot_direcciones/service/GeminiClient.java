package ec.edu.espe.chatbot_direcciones.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GeminiClient {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String geminiUrl;

    public String generarInstruccionLLM(String origen, String destino, String baseInstruccion) {
        String prompt = "Estoy desarrollando un asistente virtual en una universidad. " +
                "Responde de manera breve y cordial con indicaciones para llegar desde \"" + origen +
                "\" hasta \"" + destino + "\". Información de la ruta: " + baseInstruccion;

        Map<String, Object> message = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", prompt)))
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(message, headers);

        String fullUrl = geminiUrl + "?key=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();

        try {
            Map response = restTemplate.postForObject(fullUrl, request, Map.class);
            Map candidates = ((List<Map>) response.get("candidates")).get(0);
            Map content = (Map) candidates.get("content");
            List<Map> parts = (List<Map>) content.get("parts");
            return parts.get(0).get("text").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "⚠️ No se pudo generar respuesta dinámica.";
        }
    }
}
