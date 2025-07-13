package ec.edu.espe.chatbot_direcciones.repository;

import ec.edu.espe.chatbot_direcciones.model.Punto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PuntoRepository extends JpaRepository<Punto, Long> {
    Optional<Punto> findByNombreIgnoreCase(String nombre);
}
