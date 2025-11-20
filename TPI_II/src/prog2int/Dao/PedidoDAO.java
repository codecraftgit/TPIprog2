package prog2int.Dao;

import prog2int.Config.DatabaseConnection;
import prog2int.Models.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO Pedido.
 * - CRUD con soft delete (eliminado = TRUE)
 * - Búsqueda por número (único de negocio)
 */
public class PedidoDAO implements GenericDAO<Pedido> {

    /* ===================== SQL ===================== */
    private static final String INSERT_SQL =
            "INSERT INTO Pedido (eliminado, numero, fecha, clienteNombre, total, estado, envio) " +
                    "VALUES (FALSE, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_SQL =
            "UPDATE Pedido SET numero = ?, fecha = ?, clienteNombre = ?, total = ?, estado = ? " +
                    "WHERE id = ? AND eliminado = FALSE";

    private static final String DELETE_SQL =
            "UPDATE Pedido SET eliminado = TRUE WHERE id = ? AND eliminado = FALSE";

    private static final String BASE_SELECT =
            "SELECT id, eliminado, numero, fecha, clienteNombre, total, estado, envio " +
                    "FROM Pedido ";

    private static final String SELECT_BY_ID_SQL =
            BASE_SELECT + "WHERE id = ? AND eliminado = FALSE";

    private static final String SELECT_ALL_SQL =
            BASE_SELECT + "WHERE eliminado = FALSE";

    private static final String SELECT_BY_NUMERO_SQL =
            BASE_SELECT + "WHERE eliminado = FALSE AND numero = ?";

    /* ===================== CRUD ===================== */

    @Override
    public void insertar(Pedido p) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            
                ps.setString(1, p.getNumero());
                ps.setObject(2, p.getFecha());
                ps.setString(3, p.getClienteNombre());
                ps.setDouble(4, p.getTotal());
                ps.setString(5, p.getEstado().name());
                
                if (p.getEnvio() != null) {
                    ps.setLong(6, p.getEnvio().getId());
                } else {
                    ps.setNull(6, Types.INTEGER);
                }
            
                int filasAfectadas = ps.executeUpdate();
                if(filasAfectadas > 0) {
                    try(ResultSet generatedKeys = ps.getGeneratedKeys()) {
                        if (generatedKeys.next()) {                       
                            conn.commit();                        
                        }
                    }
            }
            setGeneratedId(ps, p);           
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("Error al hacer insert, transaccion revertida");
        } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    @Override
    public void insertTx(Pedido p, Connection conn) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            setParams(ps, p);
            ps.executeUpdate();
            setGeneratedId(ps, p);
        }
    }

    @Override
    public void actualizar(Pedido p) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            setParams(ps, p);
            ps.setLong(6, p.getId());

            int rows = ps.executeUpdate();
            if (rows == 0) throw new SQLException("No se actualizo Pedido id=" + p.getId());
        }
    }

    @Override
    public void eliminar(long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
            ps.setLong(1, id);
            int rows = ps.executeUpdate();
            if (rows == 0) throw new SQLException("No se encontro Pedido activo id=" + id);
        }
    }

    @Override
    public Pedido getById(long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    @Override
    public List<Pedido> getAll() throws Exception {
        List<Pedido> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(SELECT_ALL_SQL)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    /* ===================== Búsquedas especializadas ===================== */

    /** Busca un pedido por su número (campo único de negocio). */
    public Pedido getByNumero(String numero) throws Exception {
        if (numero == null || numero.trim().isEmpty())
            throw new IllegalArgumentException("numero no puede estar vacio");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_NUMERO_SQL)) {
            ps.setString(1, numero.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /* ===================== Helpers ===================== */

    private void setParams(PreparedStatement ps, Pedido p) throws SQLException {
        ps.setString(1, p.getNumero());
        setLocalDate(ps, 2, p.getFecha());
        ps.setString(3, p.getClienteNombre());
        ps.setDouble(4, p.getTotal());
        ps.setString(5, p.getEstado() != null ? p.getEstado().name() : null);
    }

    private void setGeneratedId(PreparedStatement ps, Pedido p) throws SQLException {
        try (ResultSet keys = ps.getGeneratedKeys()) {
            if (keys.next()) p.setId(keys.getLong(1));
            else throw new SQLException("INSERT Pedido sin ID generado");
        }
    }

    private void setLocalDate(PreparedStatement ps, int idx, LocalDate date) throws SQLException {
        if (date == null) ps.setNull(idx, Types.DATE);
        else ps.setDate(idx, Date.valueOf(date));
    }

    private Pedido map(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setId(rs.getLong("id"));
        p.setEliminado(rs.getBoolean("eliminado"));
        p.setNumero(rs.getString("numero"));
        Date f = rs.getDate("fecha");
        if (f != null) p.setFecha(f.toLocalDate());
        p.setClienteNombre(rs.getString("clienteNombre"));
        p.setTotal(rs.getDouble("total"));
        String est = rs.getString("estado");
        if (est != null) p.setEstado(EstadoPedido.valueOf(est));
        long envioID = rs.getLong("envio");
        Envio envio = new Envio();
        envio.setId(envioID);
        p.setEnvio(envio);
        
        return p;
    }
}
