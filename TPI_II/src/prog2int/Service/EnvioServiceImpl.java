package prog2int.Service;

import prog2int.Dao.GenericDAO;
import prog2int.Models.Empresa;
import prog2int.Models.Envio;
import prog2int.Models.EstadoEnvio;
import prog2int.Models.TipoEnvio;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio de negocio para Envio.
 * Responsabilidades:
 * - Validar campos obligatorios y reglas de consistencia antes de persistir
 * - Delegar a DAO las operaciones CRUD
 */
public class EnvioServiceImpl implements GenericService<Envio> {

    private final GenericDAO<Envio> envioDAO;

    public EnvioServiceImpl(GenericDAO<Envio> envioDAO) {
        if (envioDAO == null) throw new IllegalArgumentException("EnvioDAO no puede ser null");
        this.envioDAO = envioDAO;
    }

    @Override
    public void insertar(Envio envio) throws Exception {
        validateEnvio(envio);
        envioDAO.insertar(envio);
    }

    @Override
    public void actualizar(Envio envio) throws Exception {
        validateEnvio(envio);
        if (envio.getId() <= 0) throw new IllegalArgumentException("El ID de Envio debe ser > 0 para actualizar");
        envioDAO.actualizar(envio);
    }

    @Override
    public void eliminar(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("El ID debe ser mayor a 0");
        envioDAO.eliminar(id);
    }

    @Override
    public Envio getById(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("El ID debe ser mayor a 0");
        return envioDAO.getById(id);
    }

    @Override
    public List<Envio> getAll() throws Exception {
        return envioDAO.getAll();
    }

    /* ================== Validaciones de negocio ================== */

    private void validateEnvio(Envio e) {
        if (e == null) throw new IllegalArgumentException("El envío no puede ser null");

        // Enums obligatorios
        if (e.getEmpresa() == null) throw new IllegalArgumentException("La empresa es obligatoria");
        if (e.getTipo() == null) throw new IllegalArgumentException("El tipo de envio es obligatorio");
        if (e.getEstado() == null) throw new IllegalArgumentException("El estado del envio es obligatorio");

        // Dominio de enums (defensivo si vienen de UI)
        ensureEnum(e.getEmpresa(), Empresa.class, "empresa invalida");
        ensureEnum(e.getTipo(),    TipoEnvio.class, "tipo de envio invalido");
        ensureEnum(e.getEstado(),  EstadoEnvio.class, "estado de envio invalido");

        // tracking opcional, pero si viene debe respetar longitud
        if (e.getTracking() != null && e.getTracking().trim().length() > 40) {
            throw new IllegalArgumentException("tracking supera los 40 caracteres");
        }

        // costo requerido y no negativo
        if (e.getCosto() < 0) throw new IllegalArgumentException("El costo no puede ser negativo");

        // coherencia de fechas: estimada >= despacho (cuando ambas están)
        LocalDate fd = e.getFechaDespacho();
        LocalDate fe = e.getFechaEstimada();
        if (fd != null && fe != null && fe.isBefore(fd)) {
            throw new IllegalArgumentException("La fecha estimada no puede ser anterior a la de despacho");
        }
    }

    private <E extends Enum<E>> void ensureEnum(E value, Class<E> type, String msgOnError) {
        try {
            Enum.valueOf(type, value.name());
        } catch (Exception ex) {
            throw new IllegalArgumentException(msgOnError);
        }
    }
}
