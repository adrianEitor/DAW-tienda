import java.math.BigDecimal;
import java.sql.Timestamp;

public class Pedido {
    private int id;
    private int usuarioId;
    private Float importeTotal;
    private Timestamp fechaPedido;

    public Pedido(int id, int usuarioId, Float importeTotal, Timestamp fechaPedido) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.importeTotal = importeTotal;
        this.fechaPedido = fechaPedido;
    }

    public Pedido(int usuarioId, Float importeTotal) {
        this.usuarioId = usuarioId;
        this.importeTotal = importeTotal;
    }

    public int getId() {
        return id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Float getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(Float importeTotal) {
        this.importeTotal = importeTotal;
    }

    public Timestamp getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Timestamp fechaPedido) {
        this.fechaPedido = fechaPedido;
    }
}
