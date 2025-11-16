package prog2int.Models;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Entidad que representa un Envío.
 * Hereda de Base para obtener id y eliminado (baja lógica).
 *
 * Relación con Pedido:
 * - Asociación 1↔1 (opcional desde Pedido: un Pedido puede tener 0 o 1 Envío).
 * - En la BD, se implementa con FK UNIQUE (pedido.envio_id).
 *
 * Tabla BD: Envio
 * Campos:
 * - id: BIGINT AUTO_INCREMENT PRIMARY KEY (heredado de Base)
 * - eliminado: BOOLEAN DEFAULT FALSE (heredado de Base)
 * - tracking: VARCHAR(40) UNIQUE
 * - empresa: ENUM('ANDREANI','OCA','CORREO_ARG') NOT NULL
 * - tipo: ENUM('ESTANDAR','EXPRES') NOT NULL
 * - costo: DECIMAL(10,2) NOT NULL
 * - fechaDespacho: DATE
 * - fechaEstimada: DATE
 * - estado: ENUM('EN_PREPARACION','EN_TRANSITO','ENTREGADO') NOT NULL
 */
public class Envio extends Base {
    /** Código de seguimiento. Puede ser null; si no, debe ser único. */
    private String tracking;

    /** Empresa de envío. Requerido. */
    private Empresa empresa;

    /** Tipo de envío. Requerido. */
    private TipoEnvio tipo;

    /** Costo del envío. Requerido. */
    private double costo;

    /** Fecha en que se despacha el envío. */
    private LocalDate fechaDespacho;

    /** Fecha estimada de entrega. */
    private LocalDate fechaEstimada;

    /** Estado logístico del envío. Requerido. */
    private EstadoEnvio estado;

    /** Constructor completo (reconstrucción desde BD). */
    public Envio(long id, boolean eliminado, String tracking, Empresa empresa, TipoEnvio tipo,
                 double costo, LocalDate fechaDespacho, LocalDate fechaEstimada, EstadoEnvio estado) {
        super(id, eliminado);
        this.tracking = tracking;
        this.empresa = Objects.requireNonNull(empresa, "empresa requerida");
        this.tipo = Objects.requireNonNull(tipo, "tipo requerido");
        this.costo = costo;
        this.fechaDespacho = fechaDespacho;
        this.fechaEstimada = fechaEstimada;
        this.estado = Objects.requireNonNull(estado, "estado requerido");
    }

    /** Constructor por defecto (entidad nueva sin ID). */
    public Envio() { super(); }

    public String getTracking() { return tracking; }
    public void setTracking(String tracking) { this.tracking = tracking; }

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    public TipoEnvio getTipo() { return tipo; }
    public void setTipo(TipoEnvio tipo) { this.tipo = tipo; }

    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }

    public LocalDate getFechaDespacho() { return fechaDespacho; }
    public void setFechaDespacho(LocalDate fechaDespacho) { this.fechaDespacho = fechaDespacho; }

    public LocalDate getFechaEstimada() { return fechaEstimada; }
    public void setFechaEstimada(LocalDate fechaEstimada) { this.fechaEstimada = fechaEstimada; }

    public EstadoEnvio getEstado() { return estado; }
    public void setEstado(EstadoEnvio estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Envio{" +
                "id=" + getId() +
                ", tracking='" + tracking + '\'' +
                ", empresa=" + empresa +
                ", tipo=" + tipo +
                ", costo=" + costo +
                ", fechaDespacho=" + fechaDespacho +
                ", fechaEstimada=" + fechaEstimada +
                ", estado=" + estado +
                ", eliminado=" + isEliminado() +
                '}';
    }

    /**
     * Igualdad semántica por tracking (campo único de negocio).
     * Si no hay tracking, no se considera igual por esta vía.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Envio)) return false;
        Envio envio = (Envio) o;
        return tracking != null && tracking.equals(envio.tracking);
    }

    @Override
    public int hashCode() {
        return tracking != null ? tracking.hashCode() : 0;
    }
}
