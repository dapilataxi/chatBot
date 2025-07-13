package ec.edu.espe.chatbot_direcciones.repository;

import ec.edu.espe.chatbot_direcciones.model.Punto;
import ec.edu.espe.chatbot_direcciones.model.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RutaRepository extends JpaRepository<Ruta, Long> {
    Optional<Ruta> findByOrigenAndDestino(Punto origen, Punto destino);
}
