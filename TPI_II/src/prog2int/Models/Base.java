package prog2int.Models;

/**
 * Clase base abstracta para todas las entidades del sistema.
 * Implementa el patrón de soft delete mediante el campo 'eliminado'.
 *
 * - Proporcionar campos comunes a todas las entidades (id, eliminado)
 * - Soportar eliminación lógica en lugar de eliminación física
 
 */
public abstract class Base {
    
    private long id;
    private boolean eliminado;

    /**
     * Constructor completo con todos los campos.
     * Usado por los DAOs al reconstruir entidades desde la base de datos.
     *
     * @param id Identificador de la entidad
     * @param eliminado Estado de eliminación
     */
    protected Base(long id, boolean eliminado) {
        this.id = id;
        this.eliminado = eliminado;
    }

    /**
     * Constructor por defecto.
     * Inicializa una entidad nueva sin ID (será asignado por la BD).
     * Por defecto, las entidades nuevas NO están eliminadas.
     */
    protected Base() {
        this.eliminado = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }
}
