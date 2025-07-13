package ec.edu.espe.chatbot_direcciones.model;

import jakarta.persistence.*;

@Entity
public class Punto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre; // ejemplo: "Biblioteca"
    private String descripcion; // ejemplo: "Edificio central, piso 2"
    private String imagenDriveId; // solo el ID, no la URL completa

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getImagenDriveId() { return imagenDriveId; }
    public void setImagenDriveId(String imagenDriveId) { this.imagenDriveId = imagenDriveId; }
}