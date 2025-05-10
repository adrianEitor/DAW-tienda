import java.io.Serializable;
import java.util.Objects;


public class CD implements Serializable{

    private static final long serialVersionUID = 1L;

    private String titulo;
    private String artista;
    private String pais;
    private float precio;
    private int cantidad;

    // Sin constructor explícito.

    // Getters
    public String getTitulo() { return titulo; }
    public String getArtista() { return artista; }
    public String getPais() { return pais; }
    public float getPrecio() { return precio; }
    public int getCantidad() { return cantidad; }

    // Setters
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setArtista(String artista) { this.artista = artista; }
    public void setPais(String pais) { this.pais = pais; }
    public void setPrecio(float precio) { this.precio = precio; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public float getImporte() {
        return precio * cantidad;
    }

      @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CD cd = (CD) o;
        // Comparación basada en título y artista para unicidad en el carrito
        return Objects.equals(titulo, cd.titulo) &&
               Objects.equals(artista, cd.artista);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo, artista);
    }

    @Override
    public String toString() {
        return "CD{" +
               "titulo='" + titulo + '\'' +
               ", artista='" + artista + '\'' +
               ", pais='" + pais + '\'' +
               ", precio=" + precio +
               ", cantidad=" + cantidad +
               '}';
    }
}