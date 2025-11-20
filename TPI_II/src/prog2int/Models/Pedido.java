package prog2int.Models;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Entidad que representa un Pedido del cliente.
 * Hereda de Base para obtener id y eliminado (baja lógica).
 *
 * Tabla BD: Pedido
 * Campos:
 * - id: BIGINT AUTO_INCREMENT PRIMARY KEY (heredado de Base)
 * - eliminado: BOOLEAN DEFAULT FALSE (heredado de Base)
 * - numero: VARCHAR(20) NOT NULL UNIQUE
 * - fecha: DATE
 * - clienteNombre: VARCHAR(120)
 * - total: DOUBLE(12,2) NOT NULL
 * - estado: ENUM('NUEVO','FACTURADO','ENVIADO') NOT NULL
 */
public class Pedido extends Base {
    
    private String numero;
    private LocalDate fecha;
    private String clienteNombre;
    private double total;
    private EstadoPedido estado;
    private Envio envio;

    /** Constructor completo (reconstrucción desde BD). */
    public Pedido(long id, boolean eliminado, String numero, LocalDate fecha,
                  String clienteNombre, double total, EstadoPedido estado) {
        super(id, eliminado);
        this.numero = Objects.requireNonNull(numero, "numero requerido");
        this.fecha = fecha;
        this.clienteNombre = clienteNombre;
        this.total = total;
        this.estado = Objects.requireNonNull(estado, "estado requerido");
    }

    /** Constructor por defecto (entidad nueva sin ID). */
    public Pedido() { super(); }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }
    
    public Envio getEnvio() { return envio; }
    public void setEnvio(Envio envio) { this.envio = envio; }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + getId() +
                ", numero='" + numero + '\'' +
                ", fecha=" + fecha +
                ", clienteNombre='" + clienteNombre + '\'' +
                ", total=" + total +
                ", estado=" + estado +
                ", envio=" + (envio != null ? envio.getId() : null) +
                ", eliminado=" + isEliminado() +
                '}';
    }

    /**
     * Igualdad semántica por número de pedido (campo único de negocio).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pedido)) return false;
        Pedido pedido = (Pedido) o;
        return Objects.equals(numero, pedido.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }
}
