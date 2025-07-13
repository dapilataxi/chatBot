package ec.edu.espe.chatbot_direcciones.model;

import jakarta.persistence.*;

@Entity
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "origen_id", nullable = false)
    private Punto origen;

    @ManyToOne
    @JoinColumn(name = "destino_id", nullable = false)
    private Punto destino;

    @Column(columnDefinition = "TEXT")
    private String instrucciones;

    // Getters y Setters
    public Long getId() { return id; }

    public Punto getOrigen() { return origen; }
    public void setOrigen(Punto origen) { this.origen = origen; }

    public Punto getDestino() { return destino; }
    public void setDestino(Punto destino) { this.destino = destino; }

    public String getInstrucciones() { return instrucciones; }
    public void setInstrucciones(String instrucciones) { this.instrucciones = instrucciones; }
}
