-- =============================================
-- TPI-Datos.sql
-- Datos de prueba para testeo (idempotentes).
-- NOTA: No se realiza INSERT sino que utilizamos REPLACE en caso de que existan filas iguales
-- =============================================
use pedido_envio_jdbc;

-- Tabla Envio
REPLACE INTO envio (id, eliminado, tracking, empresa, tipo, costo,
fechaDespacho, fechaEstimada, estado) VALUES
(1, 0, "TRK0000001", "ANDREANI", "ESTANDAR", 250.0, "2025-10-07 10:54:10", "2025-10-15 11:30:25", "EN_PREPARACION"),
(2, 0, "TRK0000002", "CORREO_ARG", "EXPRESS", 500.0, "2025-09-12 08:45:10", "2025-09-14 11:30:00", "EN_PREPARACION"),
(3, 0, "TRK0000003", "OCA", "EXPRESS", 450.0, "2025-11-14 10:18:10", "2025-11-16 10:18:10", "EN_TRANSITO");

-- Tabla Pedido
REPLACE INTO pedido (id, eliminado, numero, fecha, clienteNombre,
total, estado, envio) VALUES
(1, 0, "PD0000001", "2025-10-05 13:20:19", "Lautaro", 2500.0, "NUEVO", 1),
(2, 0, "PD0000002", "2025-09-11 15:35:10", "Benjamin", 6500.0, "FACTURADO", 2),
(3, 0, "PD0000003", "2025-11-13 10:47:28", "Gonzalo", 15000.0, "NUEVO", 3);