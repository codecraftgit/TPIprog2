package prog2int.Dao;

import java.sql.Connection;
import java.util.List;

/**
 * DAO genérico para operaciones CRUD básicas con soporte transaccional.
 * @param <T> Tipo de entidad
 */
public interface GenericDAO<T> {
    void insertar(T entity) throws Exception;

    /** Inserción usando una Connection externa (NO cerrar conn aquí). */
    void insertTx(T entity, Connection conn) throws Exception;

    void actualizar(T entity) throws Exception;

    /** Soft delete (eliminado = TRUE). */
    void eliminar(long id) throws Exception;

    T getById(long id) throws Exception;

    List<T> getAll() throws Exception;
}
