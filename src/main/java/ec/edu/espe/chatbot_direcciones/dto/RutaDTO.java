package ec.edu.espe.chatbot_direcciones.dto;

public class RutaDTO {
    private String instrucciones;
    private String imagenOrigen;
    private String imagenDestino;

    // Getters y Setters
    public String getInstrucciones() { return instrucciones; }
    public void setInstrucciones(String instrucciones) { this.instrucciones = instrucciones; }

    public String getImagenOrigen() { return imagenOrigen; }
    public void setImagenOrigen(String imagenOrigen) { this.imagenOrigen = imagenOrigen; }

    public String getImagenDestino() { return imagenDestino; }
    public void setImagenDestino(String imagenDestino) { this.imagenDestino = imagenDestino; }
}