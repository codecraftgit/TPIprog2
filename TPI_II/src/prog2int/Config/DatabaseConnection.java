package prog2int.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Patrón: Factory con configuración estática
 * - No se puede instanciar (constructor privado)
 * - Proporciona conexiones mediante método estático getConnection()
 * - Configuración cargada una sola vez en bloque static
 *
 * Configuración por defecto:
 * - URL: jdbc:mysql://localhost:3306/NOMBRE_BASE_DE_DATOS
 * - Usuario: root
 * - Contraseña: vacía (común en desarrollo local)
 *
 */
public final class DatabaseConnection {
    /** URL de conexión JDBC.*/
    private static final String URL = System.getProperty("db.url", "jdbc:mysql://localhost:3307/pedido_envio_jdbc");

    /** Usuario de la base de datos.*/
    private static final String USER = System.getProperty("db.user", "root");

    /** Contraseña del usuario. UTILIZAR CONTRASEÑA PROPIA de usuario ROOT */
    private static final String PASSWORD = System.getProperty("db.password", "Excavatorutn19!");

    /**
     * Bloque de inicialización estática.
     * Se ejecuta UNA SOLA VEZ cuando la clase se carga en memoria.
     *
     * Acciones:
     * 1. Carga el driver JDBC de MySQL
     * 2. Valida que la configuración sea correcta
     *
     * Si falla, lanza ExceptionInInitializerError y detiene la aplicación.
     * Esto es intencional: sin BD correcta, la app no puede funcionar.
     */
    static {
        try {
            // Carga explícita del driver (requerido en algunas versiones de Java)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Valida configuración tempranamente (fail-fast)
            validateConfiguration();
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("Error: No se encontró el driver JDBC de MySQL: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new ExceptionInInitializerError("Error en la configuración de la base de datos: " + e.getMessage());
        }
    }

    /**
     * Esta es una clase utilitaria con solo métodos estáticos.
     */
    private DatabaseConnection() {
        throw new UnsupportedOperationException("Esta es una clase utilitaria y no debe ser instanciada");
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Valida que los parámetros de configuración sean válidos.
     * Llamado una sola vez desde el bloque static.
     * @throws IllegalStateException Si la configuración es inválida
     */
    private static void validateConfiguration() {
        if (URL == null || URL.trim().isEmpty()) {
            throw new IllegalStateException("La URL de la base de datos no está configurada");
        }
        if (USER == null || USER.trim().isEmpty()) {
            throw new IllegalStateException("El usuario de la base de datos no está configurado");
        }
        // PASSWORD puede ser vacío (en caso de haber asignado una a usuario ROOT)
        
        if (PASSWORD == null) {
            throw new IllegalStateException("La contraseña de la base de datos no está configurada");
        }
    }
}