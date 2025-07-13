package ec.edu.espe.chatbot_direcciones.controller;

import ec.edu.espe.chatbot_direcciones.dto.RutaDTO;
import ec.edu.espe.chatbot_direcciones.service.DireccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/webhook")
public class DireccionController {

    @Autowired
    private DireccionService direccionService;

    @PostMapping("/chatbase")
    public Map<String, Object> webhookChatbase(@RequestBody Map<String, Object> payload) {
        try {
            Map<String, Object> queryResult = (Map<String, Object>) payload.get("queryResult");
            Map<String, Object> parameters = (Map<String, Object>) queryResult.get("parameters");

            String origen = "";
            Object origenObj = parameters.get("origen");
            if (origenObj instanceof List) {
                List<?> origenList = (List<?>) origenObj;
                if (!origenList.isEmpty()) origen = origenList.get(0).toString();
            } else if (origenObj != null) origen = origenObj.toString();

            String destino = "";
            Object destinoObj = parameters.get("destino");
            if (destinoObj instanceof List) {
                List<?> destinoList = (List<?>) destinoObj;
                if (!destinoList.isEmpty()) destino = destinoList.get(0).toString();
            } else if (destinoObj != null) destino = destinoObj.toString();

            if (origen.isEmpty() || destino.isEmpty()) {
                return Map.of("fulfillmentText", "⚠️ Por favor indica tanto el origen como el destino.");
            }

            RutaDTO ruta = direccionService.obtenerRuta(origen, destino);

            List<Map<String, Object>> fulfillmentMessages = new ArrayList<>();
            fulfillmentMessages.add(Map.of(
                    "text", Map.of("text", List.of("📍 Ruta desde *" + origen + "* hasta *" + destino + "*:"))
            ));
            fulfillmentMessages.add(Map.of(
                    "text", Map.of("text", List.of(ruta.getInstrucciones()))
            ));
            fulfillmentMessages.add(Map.of(
                    "image", Map.of("imageUri", ruta.getImagenOrigen())
            ));
            fulfillmentMessages.add(Map.of(
                    "image", Map.of("imageUri", ruta.getImagenDestino())
            ));

            Map<String, Object> response = new HashMap<>();
            response.put("fulfillmentMessages", fulfillmentMessages);
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("fulfillmentText", "⚠️ Error en el servidor: " + e.getMessage());
        }
    }
}
