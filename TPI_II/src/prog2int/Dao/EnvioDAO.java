package prog2int.Dao;

import prog2int.Config.DatabaseConnection;
import prog2int.Models.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO Envio.
 * - CRUD con soft delete (eliminado = TRUE)
 * - Enums mapeados con valueOf() (coinciden con ENUM de MySQL)
 * - Fechas con LocalDate (usa DATE en BD)
 */
public class EnvioDAO implements GenericDAO<Envio> {

    private static final String INSERT_SQL =
            "INSERT INTO Envio (eliminado, tracking, empresa, tipo, costo, fechaDespacho, fechaEstimada, estado) " +
                    "VALUES (FALSE, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_SQL =
            "UPDATE Envio SET tracking = ?, empresa = ?, tipo = ?, costo = ?, fechaDespacho = ?, fechaEstimada = ?, estado = ? " +
                    "WHERE id = ? AND eliminado = FALSE";

    private static final String DELETE_SQL =
            "UPDATE Envio SET eliminado = TRUE WHERE id = ? AND eliminado = FALSE";

    private static final String SELECT_BY_ID_SQL =
            "SELECT id, eliminado, tracking, empresa, tipo, costo, fechaDespacho, fechaEstimada, estado " +
                    "FROM Envio WHERE id = ? AND eliminado = FALSE";

    private static final String SELECT_ALL_SQL =
            "SELECT id, eliminado, tracking, empresa, tipo, costo, fechaDespacho, fechaEstimada, estado " +
                    "FROM Envio WHERE eliminado = FALSE";

    @Override
    public void insertar(Envio envio) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            

            setParams(ps, envio);
            int filasAfectadas = ps.executeUpdate();
            if(filasAfectadas > 0) {
                try(ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {                       
                        conn.commit();                        
                    }
                }
            }
            setGeneratedId(ps, envio);           
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("Error al hacer insert, transaccion revertida");
        } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    @Override
    public void insertTx(Envio envio, Connection conn) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            setParams(ps, envio);
            ps.executeUpdate();
            setGeneratedId(ps, envio);
        }
    }

    @Override
    public void actualizar(Envio envio) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, envio.getTracking());
            ps.setString(2, envio.getEmpresa() != null ? envio.getEmpresa().name() : null);
            ps.setString(3, envio.getTipo() != null ? envio.getTipo().name() : null);
            ps.setDouble(4, envio.getCosto());
            setLocalDate(ps, 5, envio.getFechaDespacho());
            setLocalDate(ps, 6, envio.getFechaEstimada());
            ps.setString(7, envio.getEstado() != null ? envio.getEstado().name() : null);
            ps.setLong(8, envio.getId());

            int rows = ps.executeUpdate();
            if (rows == 0) throw new SQLException("No se actualizo Envio id=" + envio.getId());
        }
    }

    @Override
    public void eliminar(long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
            ps.setLong(1, id);
            int rows = ps.executeUpdate();
            if (rows == 0) throw new SQLException("No se encontro Envio activo id=" + id);
        }
    }

    @Override
    public Envio getById(long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    @Override
    public List<Envio> getAll() throws Exception {
        List<Envio> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(SELECT_ALL_SQL)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    /* ===================== Helpers ===================== */

    private void setParams(PreparedStatement ps, Envio e) throws SQLException {
        ps.setString(1, e.getTracking());
        ps.setString(2, e.getEmpresa() != null ? e.getEmpresa().name() : null);
        ps.setString(3, e.getTipo() != null ? e.getTipo().name() : null);
        ps.setDouble(4, e.getCosto());
        setLocalDate(ps, 5, e.getFechaDespacho());
        setLocalDate(ps, 6, e.getFechaEstimada());
        ps.setString(7, e.getEstado() != null ? e.getEstado().name() : null);
    }

    private void setGeneratedId(PreparedStatement ps, Envio e) throws SQLException {
        try (ResultSet keys = ps.getGeneratedKeys()) {
            if (keys.next()) e.setId(keys.getLong(1));
            else throw new SQLException("INSERT Envio sin ID generado");
        }
    }

    private void setLocalDate(PreparedStatement ps, int idx, LocalDate date) throws SQLException {
        if (date == null) ps.setNull(idx, Types.DATE);
        else ps.setDate(idx, Date.valueOf(date));
    }

    private Envio map(ResultSet rs) throws SQLException {
        Envio e = new Envio();
        e.setId(rs.getLong("id"));
        e.setEliminado(rs.getBoolean("eliminado"));
        e.setTracking(rs.getString("tracking"));

        String empresa = rs.getString("empresa");
        if (empresa != null) e.setEmpresa(Empresa.valueOf(empresa));

        String tipo = rs.getString("tipo");
        if (tipo != null) e.setTipo(TipoEnvio.valueOf(tipo));

        e.setCosto(rs.getDouble("costo"));

        Date fd = rs.getDate("fechaDespacho");
        if (fd != null) e.setFechaDespacho(fd.toLocalDate());

        Date fe = rs.getDate("fechaEstimada");
        if (fe != null) e.setFechaEstimada(fe.toLocalDate());

        String estado = rs.getString("estado");
        if (estado != null) e.setEstado(EstadoEnvio.valueOf(estado));

        return e;
    }
}
