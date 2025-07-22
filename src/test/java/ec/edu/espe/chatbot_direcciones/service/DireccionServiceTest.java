package ec.edu.espe.chatbot_direcciones.service;

import ec.edu.espe.chatbot_direcciones.dto.RutaDTO;
import ec.edu.espe.chatbot_direcciones.model.Punto;
import ec.edu.espe.chatbot_direcciones.model.Ruta;
import ec.edu.espe.chatbot_direcciones.repository.PuntoRepository;
import ec.edu.espe.chatbot_direcciones.repository.RutaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DireccionServiceTest {

    @Mock
    private PuntoRepository puntoRepository;

    @Mock
    private RutaRepository rutaRepository;

    @Mock
    private GeminiClient geminiClient;

    @InjectMocks
    private DireccionService direccionService;

    private Punto origen;
    private Punto destino;
    private Ruta ruta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        origen = new Punto();
        origen.setId(1L);
        origen.setNombre("Biblioteca");
        origen.setImagenDriveId("imagen1");

        destino = new Punto();
        destino.setId(2L);
        destino.setNombre("Laboratorio");
        destino.setImagenDriveId("imagen2");

        ruta = new Ruta();
        ruta.setOrigen(origen);
        ruta.setDestino(destino);
        ruta.setInstrucciones("Ir recto y girar a la izquierda");
    }

    @Test
    void testObtenerRutaExitosa() {
        when(puntoRepository.findByNombreIgnoreCase("Biblioteca")).thenReturn(Optional.of(origen));
        when(puntoRepository.findByNombreIgnoreCase("Laboratorio")).thenReturn(Optional.of(destino));
        when(rutaRepository.findByOrigenAndDestino(origen, destino)).thenReturn(Optional.of(ruta));
        when(geminiClient.generarInstruccionLLM(any(), any(), any())).thenReturn("Instrucciones generadas");

        RutaDTO dto = direccionService.obtenerRuta("Biblioteca", "Laboratorio");

        assertEquals("Instrucciones generadas", dto.getInstrucciones());
        assertEquals("https://drive.google.com/uc?id=imagen1", dto.getImagenOrigen());
        assertEquals("https://drive.google.com/uc?id=imagen2", dto.getImagenDestino());
    }

    @Test
    void testOrigenNoEncontrado() {
        when(puntoRepository.findByNombreIgnoreCase("Biblioteca")).thenReturn(Optional.empty());
        Exception ex = assertThrows(RuntimeException.class, () -> {
            direccionService.obtenerRuta("Biblioteca", "Laboratorio");
        });
        assertTrue(ex.getMessage().contains("Punto de origen no encontrado"));
    }

    @Test
    void testDestinoNoEncontrado() {
        when(puntoRepository.findByNombreIgnoreCase("Biblioteca")).thenReturn(Optional.of(origen));
        when(puntoRepository.findByNombreIgnoreCase("Laboratorio")).thenReturn(Optional.empty());
        Exception ex = assertThrows(RuntimeException.class, () -> {
            direccionService.obtenerRuta("Biblioteca", "Laboratorio");
        });
        assertTrue(ex.getMessage().contains("Punto de destino no encontrado"));
    }

    @Test
    void testRutaNoRegistrada() {
        when(puntoRepository.findByNombreIgnoreCase("Biblioteca")).thenReturn(Optional.of(origen));
        when(puntoRepository.findByNombreIgnoreCase("Laboratorio")).thenReturn(Optional.of(destino));
        when(rutaRepository.findByOrigenAndDestino(origen, destino)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () -> {
            direccionService.obtenerRuta("Biblioteca", "Laboratorio");
        });
        assertTrue(ex.getMessage().contains("Ruta no registrada"));
    }

    @Test
    void testOrigenIgualADestino() {
        Exception ex = assertThrows(RuntimeException.class, () -> {
            direccionService.obtenerRuta("Biblioteca", "Biblioteca");
        });
        assertTrue(ex.getMessage().contains("⚠️ El punto de origen y destino no pueden ser iguales."));
    }
}
