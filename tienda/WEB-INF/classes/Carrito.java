import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Carrito implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<CD> items;

    // NO HAY CONSTRUCTOR EXPLICITO

    // Getters y Setters
    public List<CD> getItems() {
        if (this.items == null) {
            this.items = new ArrayList<>(); 
        }
        return items;
    }

    public void setItems(List<CD> items) {
        this.items = items;
    }

    // Métodos de negocio del carrito
    public void agregarItem(CD nuevoCd) {
        Objects.requireNonNull(nuevoCd, "El CD a agregar no puede ser nulo.");
        // Asegurar que la lista 'items' esté inicializada
        if (this.items == null) {
            this.items = new ArrayList<>();
        }

        for (CD cdExistente : this.items) {
            // Usar el método equals de CD para la comparación
            if (cdExistente.equals(nuevoCd)) {
                cdExistente.setCantidad(cdExistente.getCantidad() + nuevoCd.getCantidad());
                return;
            }
        }
        this.items.add(nuevoCd);
    }

    public void eliminarItem(int index) {
        if (this.items != null && index >= 0 && index < this.items.size()) {
            CD cd = this.items.get(index);
            if (cd.getCantidad() > 1) {
                cd.setCantidad(cd.getCantidad() - 1);
            } else {
                this.items.remove(index);
            }
        }
    }

    public void vaciarCarrito() {
        if (this.items != null) {
            this.items.clear();
        }
    }

    public float getTotalGeneral() {
        float total = 0.0f;
        if (this.items != null) {
            for (CD cd : this.items) {
                total += cd.getImporte();
            }
        }
        return total;
    }

    // Propiedad calculada para EL: ${carrito.vacio}
    public boolean isVacio() {
        return this.items == null || this.items.isEmpty();
    }
}