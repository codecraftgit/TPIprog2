package prog2int.Service;

import prog2int.Dao.PedidoDAO;
import prog2int.Models.Pedido;

import java.util.List;

/**
 * Servicio de negocio para Pedido.
 * Responsabilidades:
 * - Validar campos obligatorios y reglas de consistencia (RN de dominio)
 * - Garantizar unicidad de "numero" (campo único de negocio)
 */
public class PedidoServiceImpl implements GenericService<Pedido> {

    private final PedidoDAO pedidoDAO;

    /**
     * @param pedidoDAO    DAO concreto de Pedido
     * @param envioService
     */
    public PedidoServiceImpl(PedidoDAO pedidoDAO, EnvioServiceImpl envioService) {
        if (pedidoDAO == null) throw new IllegalArgumentException("PedidoDAO no puede ser null");
        this.pedidoDAO = pedidoDAO;
    }

    @Override
    public void insertar(Pedido p) throws Exception {
        validatePedido(p);
        validateNumeroUnique(p.getNumero(), null);
        pedidoDAO.insertar(p);
    }

    @Override
    public void actualizar(Pedido p) throws Exception {
        validatePedido(p);
        if (p.getId() <= 0) throw new IllegalArgumentException("El ID del Pedido debe ser > 0 para actualizar");
        validateNumeroUnique(p.getNumero(), (int) p.getId());
        pedidoDAO.actualizar(p);
    }

    @Override
    public void eliminar(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("El ID debe ser mayor a 0");
        pedidoDAO.eliminar(id);
    }

    @Override
    public Pedido getById(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("El ID debe ser mayor a 0");
        return pedidoDAO.getById(id);
    }

    @Override
    public List<Pedido> getAll() throws Exception {
        return pedidoDAO.getAll();
    }

    /* ================== Métodos de negocio adicionales ================== */

    /** Búsqueda por número (campo único de negocio). */
    public Pedido getByNumero(String numero) throws Exception {
        if (numero == null || numero.trim().isEmpty())
            throw new IllegalArgumentException("El número no puede estar vacío");
        return pedidoDAO.getByNumero(numero.trim());
    }

    /* ================== Validaciones de negocio ================== */

    private void validatePedido(Pedido p) {
        if (p == null) throw new IllegalArgumentException("El pedido no puede ser null");

        // numero: obligatorio y longitud ≤ 20
        if (p.getNumero() == null || p.getNumero().trim().isEmpty()) {
            throw new IllegalArgumentException("El número de pedido es obligatorio");
        }
        if (p.getNumero().trim().length() > 20) {
            throw new IllegalArgumentException("El número de pedido supera los 20 caracteres");
        }

        // total: requerido no negativo (si necesitás > 0, cambiar la regla)
        if (p.getTotal() < 0) {
            throw new IllegalArgumentException("El total no puede ser negativo");
        }

        // estado: obligatorio
        if (p.getEstado() == null) {
            throw new IllegalArgumentException("El estado del pedido es obligatorio");
        }
    }

    /**
     * Garantiza unicidad del campo número.
     * @param numero número a validar
     * @param pedidoId Id actual si es UPDATE; null si es INSERT
     */
    private void validateNumeroUnique(String numero, Integer pedidoId) throws Exception {
        Pedido existente = pedidoDAO.getByNumero(numero);
        if (existente != null) {
            if (pedidoId == null || existente.getId() != pedidoId) {
                throw new IllegalArgumentException("Ya existe un pedido con el número: " + numero);
            }
        }
    }
}
