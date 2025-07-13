package ec.edu.espe.chatbot_direcciones.service;

import ec.edu.espe.chatbot_direcciones.dto.RutaDTO;
import ec.edu.espe.chatbot_direcciones.model.Punto;
import ec.edu.espe.chatbot_direcciones.model.Ruta;
import ec.edu.espe.chatbot_direcciones.repository.PuntoRepository;
import ec.edu.espe.chatbot_direcciones.repository.RutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DireccionService {

    @Autowired
    private PuntoRepository puntoRepository;

    @Autowired
    private RutaRepository rutaRepository;

    @Autowired
    private GeminiClient geminiClient;

    public RutaDTO obtenerRuta(String origenNombre, String destinoNombre) {
        Punto puntoOrigen = puntoRepository.findByNombreIgnoreCase(origenNombre)
                .orElseThrow(() -> new RuntimeException("Punto de origen no encontrado: " + origenNombre));
        Punto puntoDestino = puntoRepository.findByNombreIgnoreCase(destinoNombre)
                .orElseThrow(() -> new RuntimeException("Punto de destino no encontrado: " + destinoNombre));

        Ruta ruta = rutaRepository.findByOrigenAndDestino(puntoOrigen, puntoDestino)
                .orElseThrow(() -> new RuntimeException("Ruta no registrada entre " + origenNombre + " y " + destinoNombre));

        RutaDTO dto = new RutaDTO();

        // 🔥 LLM genera la instrucción con más naturalidad
        String instruccionLLM = geminiClient.generarInstruccionLLM(
                origenNombre,
                destinoNombre,
                ruta.getInstrucciones()
        );

        dto.setInstrucciones(instruccionLLM);
        dto.setImagenOrigen("https://drive.google.com/uc?id=" + puntoOrigen.getImagenDriveId());
        dto.setImagenDestino("https://drive.google.com/uc?id=" + puntoDestino.getImagenDriveId());

        return dto;
    }

}
