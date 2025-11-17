-- =============================================
-- TPI-Esquema.sql
-- Esquema y tablas con cláusulas IF NOT EXISTS (idempotente).
-- NOTA: No se hace DROP TABLE para no perder datos.
-- =============================================

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- Esquema
CREATE SCHEMA IF NOT EXISTS `pedido_envio_jdbc` DEFAULT CHARACTER SET utf8;
USE `pedido_envio_jdbc`;

-- Tabla Envio
CREATE TABLE IF NOT EXISTS `Envio` (
  `id` BIGINT(64) NOT NULL AUTO_INCREMENT,
  `eliminado` TINYINT(1) NOT NULL DEFAULT 0,
  `tracking` VARCHAR(40) NOT NULL UNIQUE,
  `empresa` ENUM('ANDREANI', 'OCA', 'CORREO_ARG') NOT NULL,
  `tipo` ENUM('ESTANDAR', 'EXPRESS') NOT NULL,
  `costo` DOUBLE(10,2) NOT NULL,
  `fechaDespacho` DATETIME NOT NULL,
  `fechaEstimada` DATETIME NOT NULL,
  `estado` ENUM('EN_PREPARACION', 'EN_TRANSITO', 'ENTREGADO') NOT NULL,
  PRIMARY KEY (`id`)
  
) ENGINE=InnoDB;

-- Tabla Pedido
CREATE TABLE IF NOT EXISTS `Pedido` (
  `id` BIGINT(64) NOT NULL AUTO_INCREMENT,
  `eliminado` TINYINT(1) NOT NULL DEFAULT 0,
  `numero` VARCHAR(20) NOT NULL UNIQUE,
  `fecha` DATETIME NOT NULL,
  `clienteNombre` VARCHAR(120) NOT NULL,
  `total` DOUBLE(12,2) NOT NULL,
  `estado` ENUM('NUEVO', 'FACTURADO', 'ENVIADO') NOT NULL,
  `envio` BIGINT(64) NOT NULL UNIQUE,
  PRIMARY KEY (`id`),
  INDEX `envio_idx` (`envio` ASC) VISIBLE,
  CONSTRAINT `envio`
    FOREIGN KEY (`envio`) REFERENCES `Envio` (`id`)
    ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;

-- Restricciones adicionales dentro de la  logica de cada campo (ej. montos de dinero no pueden ser 0 ni negativos)
ALTER TABLE Pedido ADD CONSTRAINT total_positivo CHECK (total > 0);
ALTER TABLE Envio ADD CONSTRAINT costo_positivo CHECK (costo > 0);
ALTER TABLE Envio ADD CONSTRAINT fecha_valida CHECK (fechaEstimada >= fechaDespacho);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
