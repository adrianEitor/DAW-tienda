public class CD {
    private String titulo;
    private String artista;
    private String pais;
    private float precio;
    private int cantidad;

    public CD(String titulo, String artista, String pais, float precio, int cantidad) {
        this.titulo = titulo;
        this.artista = artista;
        this.pais = pais;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    // Getters
    public String getTitulo() { return titulo; }
    public String getArtista() { return artista; }
    public String getPais() { return pais; }
    public float getPrecio() { return precio; }
    public int getCantidad() { return cantidad; }

    // Setters
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public float getImporte() {
        return precio * cantidad;
    }
}