# Sistema de Gestión de Pedidos y Envios

## Trabajo Práctico Integrador - Programación 2

### Descripción del Proyecto

Este Trabajo Práctico Integrador tiene como objetivo demostrar la aplicación práctica de los conceptos fundamentales de Programación Orientada a Objetos y Persistencia de Datos aprendidos durante el cursado de Programación 2. El proyecto consiste en desarrollar un sistema completo de gestión de Pedidos y Envios que permita realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre estas entidades, implementando una arquitectura robusta y profesional.

### Objetivos Académicos

El desarrollo de este sistema permite aplicar y consolidar los siguientes conceptos clave de la materia:

**1. Arquitectura en Capas (Layered Architecture)**
- Implementación de separación de responsabilidades en 4 capas diferenciadas
- Capa de Presentación (Main/UI): Interacción con el usuario mediante consola
- Capa de Lógica de Negocio (Service): Validaciones y reglas de negocio
- Capa de Acceso a Datos (DAO): Operaciones de persistencia
- Capa de Modelo (Models): Representación de entidades del dominio

**2. Programación Orientada a Objetos**
- Aplicación de principios SOLID (Single Responsibility, Dependency Injection)
- Uso de herencia mediante clase abstracta Base
- Implementación de interfaces genéricas (GenericDAO, GenericService)
- Encapsulamiento con atributos privados y métodos de acceso
- Sobrescritura de métodos (equals, hashCode, toString)

**3. Persistencia de Datos con JDBC**
- Conexión a base de datos MySQL mediante JDBC
- Implementación del patrón DAO (Data Access Object)
- Uso de PreparedStatements para prevenir SQL Injection
- Gestión de transacciones con commit y rollback
- Manejo de claves autogeneradas (AUTO_INCREMENT)
- Consultas con JOIN para relaciones entre entidades

**4. Manejo de Recursos y Excepciones**
- Uso del patrón try-with-resources para gestión automática de recursos JDBC
- Implementación de AutoCloseable en TransactionManager
- Manejo apropiado de excepciones con propagación controlada
- Validación multi-nivel: base de datos y aplicación

**5. Patrones de Diseño**
- Factory Pattern (DatabaseConnection)
- Service Layer Pattern (separación lógica de negocio)
- DAO Pattern (abstracción del acceso a datos)
- Soft Delete Pattern (eliminación lógica de registros)
- Dependency Injection manual

**6. Validación de Integridad de Datos**
- Validación de unicidad
- Validación de campos obligatorios en múltiples niveles
- Validación de integridad referencial (Foreign Keys)
- Implementación de eliminación segura para prevenir referencias huérfanas

### Funcionalidades Implementadas

El sistema permite gestionar dos entidades principales con las siguientes operaciones:



## Requisitos del Sistema

| Componente | Versión Requerida |
|------------|-------------------|
| Java JDK | 17 o superior |
| MySQL | 8.0 o superior |
| Sistema Operativo | Windows, Linux o macOS |

## Instalación

### Configurar Base de Datos

#### Pasos para crear la Base de Datos y conectar el proyecto mediante JDBC

1. Iniciar el servidor MySQL

Podés usar XAMPP, MySQL Installer o cualquier servicio MySQL.

Registrar el puerto configurado (por defecto 3306, en este proyecto se usa 3307).

2. Abrir el SGBD MySQL

Se puede usar MySQL Workbench, DBeaver, u otro cliente MySQL.

Si no hay una conexión creada, iniciar una nueva desde:
Database → Connect to Database.

3. Configurar la conexión

Seleccionar el puerto correcto configurado previamente.

Ajustar el número si difiere del estándar.

Confirmar con OK e ingresar la contraseña del usuario root (si aplica).

4. Crear la base de datos

Ir a File → Open SQL Script.

Seleccionar el archivo TPI-Esquema.sql.

Ejecutarlo.

✔ Este script es idempotente: puede ejecutarse varias veces sin causar duplicados.
✔ Crea la BD pedido_envio_jdbc, las tablas (pedido y envio) y sus restricciones.

5. Cargar datos de prueba

Abrir el archivo TPI-Datos.sql.

Ejecutarlo.

✔ También es idempotente y carga registros de prueba para testeo.

6. Verificar la creación

Refrescar Schemas o Bases de Datos.

Debería aparecer el esquema:
pedido_envio_jdbc

✔ Listo para usar JDBC

Una vez creada la base y confirmados los datos, ya se puede conectar el proyecto Java mediante JDBC usando el mismo puerto, usuario y contraseña configurados arriba.



### Configurar Conexión (Opcional)

Por defecto conecta a:
- **Host**: localhost:3307
- **Base de datos**: pedido_envio_jdbc
- **Usuario**: root
- **Contraseña**: (La que hayas puesto en tu SGBD)



## Ejecución

### Opción 1: Desde IDE
1. Abrir proyecto en Netbeans o IntelliJ IDEA
2. Ejecutar clase `Main.Main`

### Opción 2: Línea de comandos

**Windows:**
```bash
# Localizar JAR de MySQL
dir /s /b %USERPROFILE%\.gradle\caches\*mysql-connector-j-8.4.0.jar

# Ejecutar (reemplazar <ruta-mysql-jar>)
java -cp "build\classes\java\main;<ruta-mysql-jar>" Main.Main
```

**Linux/macOS:**
```bash
# Localizar JAR de MySQL
find ~/.gradle/caches -name "mysql-connector-j-8.4.0.jar"

# Ejecutar (reemplazar <ruta-mysql-jar>)
java -cp "build/classes/java/main:<ruta-mysql-jar>" Main.Main
```

### Verificar Conexión

```bash
# Usar TestConexion para verificar conexión a BD
java -cp "build/classes/java/main:<ruta-mysql-jar>" Main.TestConexion
```

Salida esperada:
```
run:
Conexion exitosa a la base de datos
Usuario conectado: root@localhost
Base de datos: pedido_envio_jdbc
URL: jdbc:mysql://localhost:3307/pedido_envio_jdbc
Driver: MySQL Connector/J vmysql-connector-j-9.5.0 (Revision: a7b3c94f50efbddb9f0dd69b3e0d1aaa25305cd6)
BUILD SUCCESSFUL (total time: 0 seconds)

```

## Uso del Sistema

### Menú Principal

```
========== MENU PEDIDOS / ENVIOS =========
1. Crear pedido
2. Listar pedidos
3. Actualizar pedido
4. Eliminar pedido
5. Buscar pedido por numero
6. Crear envio independiente
7. Listar envios
8. Actualizar envio por ID
9. Eliminar envio por ID
0. Salir
Ingrese una opcion: 
```

### Operaciones Disponibles

#### 1. Crear Pedido
Solicita los siguientes datos para crear un pedido
- Numero de Pedido 
- Fecha
- Nombre de cliente
- Precio de Pedido 
- Estado de pedido 

**Ejemplo:**

Ingrese una opcion: 1
- Numero (max 20): PD0000001
- Fecha (YYYY-MM-DD): 2025-11-15
- Cliente (nombre completo, Enter para vacio): Garcia Carlos
- Total: 7000.0
- Estado de pedido (NUEVO/FACTURADO/ENVIADO): NUEVO

#### 2. Listar Pedidos
- Muestra todas los pedidos

**Ejemplo**
Ingrese una opcion: 2
ID=1 | nro=PD0000001 | estado=NUEVO | total=2500.0
ID=2 | nro=PD0000002 | estado=FACTURADO | total=6500.0
ID=3 | nro=PD0000003 | estado=NUEVO | total=15000.0

#### 3. Actualizar Pedido
- Permite buscar pedido por id
- Permite modificar numero, Fecha, nombre, apellido, precio (total), estado.
- Presionar Enter sin escribir mantiene el valor actual

**Ejemplo:**
Ingrese una opcion: 3
ID del pedido a actualizar: 1
Nuevo numero (actual: PD0000001, Enter para mantener): PD0000004
Nuevo Fecha (2025-10-05): 2025-10-15
Nuevo cliente (Lautaro, Enter para mantener): Gonzalo
Nuevo total (2500.0, Enter para mantener): 2600.0
Nuevo estado (NUEVO, Enter para mantener): enter
Pedido Actualizado.


#### 4. Eliminar Pedido
- Eliminación lógica (marca como eliminado, no borra físicamente)
- Requiere ID del pedido

**Ejemplo**
Ingrese una opcion: 4
ID del pedido a eliminar (soft delete): 1
Pedido eliminado.

#### 5. Buscar Pedido por numero
- Busca Pedido por numero de pedido

**Ejemplo**
Ingrese una opcion: 5
Numero de pedido: PD0000002
ID=2 | estado=FACTURADO | total=6500.0

#### 6. Crear envio independiente 
Permite crear un envio

**Ejemplo**
Ingrese una opcion: 6
Tracking (opcional, max 40, Enter para vacio):  TRK0000004
Empresa (ANDREANI/OCA/CORREO_ARG): OCA
Tipo envio (ESTANDAR/EXPRESS): ESTANDAR
Costo: 5000.0
Fecha despacho (YYYY-MM-DD, Enter para vacio): 2025-11-15
Fecha estimada (YYYY-MM-DD, Enter para vacio): 2025-11-17
Estado envio (EN_PREPARACION/EN_TRANSITO/ENTREGADO): EN_TRANSITO
Envio creado. ID: 6

#### 7. Listar Envios
-Lista de Envios

**Ejemplo**
Ingrese una opcion: 7
ID=1 | tracking=TRK0000001 | empresa=ANDREANI | tipo=ESTANDAR | estado=EN_PREPARACION | costo=250.0
ID=2 | tracking=TRK0000002 | empresa=CORREO_ARG | tipo=EXPRESS | estado=EN_PREPARACION | costo=500.0
ID=3 | tracking=TRK0000003 | empresa=OCA | tipo=EXPRESS | estado=EN_TRANSITO | costo=450.0
ID=4 | tracking=123465 | empresa=OCA | tipo=EXPRESS | estado=EN_TRANSITO | costo=456.0
ID=6 | tracking=TRK0000004 | empresa=OCA | tipo=ESTANDAR | estado=EN_TRANSITO | costo=5000.0


#### 8. Actualizar envio por id
Permite actulizar envio por buscando por id

**Ejemplo**
Ingrese una opcion: 8
ID del envio a actualizar: 4
Nuevo tracking (123465, Enter para mantener): TRK0000005
Nueva empresa (OCA): CORREO_ARG
Nuevo tipo (EXPRESS): ESTANDAR
Nuevo costo (456.0, Enter para mantener): 400
Nueva fecha despacho (2025-11-15): 
Nueva fecha estimada (2025-11-17): 2025-11-18
Nuevo estado (EN_TRANSITO): 
Envio actualizado.


#### 9. Eliminar Envio por id
Hace el borrado logico de un envio atraves de su id
Ingrese una opcion: 9
ID del envio a eliminar (soft delete): 6
Envio eliminado.

#### 0. Salir de Programa
Permite con el "0" se cierre la ejecucion del programa

## Arquitectura

### Estructura en Capas

```
┌─────────────────────────────────────┐
│     Main / Capa UI                  │
│  (Interacción con usuario)          │
│  AppMenu, MenuHandler, MenuDisplay  │
└───────────┬─────────────────────────┘
            │
┌───────────▼─────────────────────────┐
│     Capa Service                    │
│  (Lógica de negocio y validación)   │
│  GenericService                     │
│  EnvioServiceImpl                   │
│  PedidoServiceImpl                  │
└───────────┬─────────────────────────┘
            │
┌───────────▼─────────────────────────┐
│    Capa DAO                         │
│  (Acceso a datos)                   │
│  PedidoDAO, EnvioDAO, GenericDAO    │
└───────────┬─────────────────────────┘
            │
┌───────────▼─────────────────────────┐
│     Capa Models                     │
│  (Entidades de dominio)             │
│  Pedido, Envio, Base, Enums         │
└─────────────────────────────────────┘
```

### Componentes Principales

**Config/**
- `DatabaseConnection.java`: Gestión de conexiones JDBC con validación en inicialización estática
- `TransactionManager.java`: Manejo de transacciones con AutoCloseable

**Models/**
- `Base.java`: Clase abstracta con campos id y eliminado
- `Pedido.java`: Entidad Pedido (numero, fecha, cliente, total, estado, envio)
- `Envio.java`: Entidad Envio (tracking, empresa, tipo, costo, fechaDespacho, FechaEstimada, estado)
- `EstadoPedido.java`: Enumeration (NUEVO, FACTURADO, ENVIADO)
- `Empresa.java`: Enumeration (ANDREANI, OCA, CORREO_ARG)
- `TipoEnvio.java`: Enumeration (ESTANDAR, EXPRESS)
- `EstadoEnvio.java`: Enumeration (EN_PREPARACION, EN_TRANSITO, ENTREGADO)


**Dao/**
- `GenericDAO<T>`: Interface genérica con operaciones CRUD
- `PedidoDAO`: Implementación con queries LEFT JOIN para incluir envio
- `EnvioDAO`: Implementación para envios

**Service/**
- `GenericService<T>`: Interface genérica para servicios
- `PedidoServiceImpl`: Validaciones de pedido y coordinación con envio
- `EnvioServiceImpl`: Validaciones de envio

**Main/**
- `Main.java`: Punto de entrada
- `AppMenu.java`: Orquestador del ciclo de menú
- `MenuHandler.java`: Implementación de operaciones CRUD con captura de entrada
- `MenuDisplay.java`: Lógica de visualización de menús
- `TestConexion.java`: Utilidad para verificar conexión a BD

## Modelo de Datos

classDiagram
    class Base {
        -id: long
        -eliminado: boolean
        +getId(): long
        +isEliminado(): boolean
    }

    class Pedido {
        -numero: String
        -fecha: java.time.LocalDate
        -clienteNombre: String
        -total: double
        -estado: EstadoPedido
        -envio: Envio
    }

    class Envio {
        -tracking: String
        -empresa: Empresa
        -tipo: TipoEnvio
        -costo: double
        -fechaDespacho: java.time.LocalDate
        -fechaEstimada: java.time.LocalDate
        -estado: EstadoEnvio
    }

    %% Enumeraciones
    class EstadoPedido {
        <<enumeration>>
        NUEVO
        FACTURADO
        ENVIADO
    }

    class Empresa {
        <<enumeration>>
        ANDREANI
        OCA
        CORREO_ARG
    }

    class TipoEnvio {
        <<enumeration>>
        ESTANDAR
        EXPRES
    }

    class EstadoEnvio {
        <<enumeration>>
        EN_PREPARACION
        EN_TRANSITO
        ENTREGADO
    }

    %% Herencia
    Base <|-- Pedido
    Base <|-- Envio

    %% Asociación Pedido → Envio
    Pedido --> "1" Envio : tiene un

    %% Asociaciones de Envio con enums
    Envio --> Empresa
    Envio --> TipoEnvio
    Envio --> EstadoEnvio

    %% Asociación de Pedido con enum
    Pedido --> EstadoPedido


**Reglas:**
- Un pedido puede tener un envío asociado (relación 1–1 opcional)
- numero (numero de pedido) es único (constraint en base de datos y validación en aplicación)
- Eliminación lógica: campo `eliminado = TRUE`
- Foreign key `envio` 

## Patrones y Buenas Prácticas

### Seguridad
- **100% PreparedStatements**: Prevención de SQL injection
- **Validación multi-capa**: Service layer valida antes de persistir


### Gestión de Recursos
- **Try-with-resources**: Todas las conexiones, statements y resultsets
- **AutoCloseable**: TransactionManager cierra y hace rollback automático
- **Scanner cerrado**: En `AppMenu.run()` al finalizar

### Validaciones
- **Input trimming**: Todos los inputs usan `.trim()` inmediatamente
- **Campos obligatorios**: Validación de null y empty en service layer
- **Pedido - total_positivo**: Validación `total > 0` en todas las operaciones
- **Envio - costo_positivo**: Validación `costo > 0` en todas las operaciones
- **Envio - fecha_valida**: Validación fechaEstimada >= fechaDespacho.


### Soft Delete
- DELETE ejecuta: `UPDATE tabla SET eliminado = TRUE WHERE id = ?`
- SELECT filtra: `WHERE eliminado = FALSE`
- No hay eliminación física de datos


## Solución de Problemas

### Error: "ClassNotFoundException: com.mysql.cj.jdbc.Driver"
**Causa**: JAR de MySQL no está en classpath

**Solución**: Incluir mysql-connector-j-8.4.0.jar en el comando java -cp

### Error: "Communications link failure"
**Causa**: MySQL no está ejecutándose



### Error: "Access denied for user 'root'@'localhost'"
**Causa**: Credenciales incorrectas

**Solución**: Verificar usuario/contraseña en DatabaseConnection.java o usar -Ddb.user y -Ddb.password

### Error: "Unknown database 'dbtpi3'"
**Causa**: Base de datos no creada

**Solución**: Ejecutar script de creación de base de datos (ver sección Instalación)

### Error: "Table 'personas' doesn't exist"
**Causa**: Tablas no creadas

**Solución**: Ejecutar script de creación de tablas (ver sección Instalación)


### Conceptos de Programación 2 Demostrados

| Concepto | Implementación en el Proyecto |
|----------|-------------------------------|
| **Herencia** | Clase abstracta `Base` heredada por `Pedido` y `Envio` |
| **Polimorfismo** | Interfaces `GenericDAO<T>` y `GenericService<T>` |
| **Encapsulamiento** | Atributos privados con getters/setters en todas las entidades |
| **Abstracción** | Interfaces que definen contratos sin implementación |
| **JDBC** | Conexión, PreparedStatements, ResultSets, transacciones |
| **DAO Pattern** | `PedidoDAO`, `EnvioDAO` abstraen el acceso a datos |
| **Service Layer** | Lógica de negocio separada en `PedidoServiceImpl`, `EnvioServiceImpl` |
| **Exception Handling** | Try-catch en todas las capas, propagación controlada |
| **Resource Management** | Try-with-resources para AutoCloseable (Connection, Statement, ResultSet) |
| **Dependency Injection** | Construcción manual de dependencias en `AppMenu.createPedidiService()` |

## Contexto Académico

**Materia**: Programación 2
**Tipo de Evaluación**: Trabajo Práctico Integrador (TPI)
**Modalidad**: Desarrollo de sistema CRUD con persistencia en base de datos
**Objetivo**: Aplicar conceptos de POO, JDBC, arquitectura en capas y patrones de diseño

Este proyecto representa la integración de todos los conceptos vistos durante el cuatrimestre, demostrando capacidad para:
- Diseñar sistemas con arquitectura profesional
- Implementar persistencia de datos con JDBC
- Aplicar patrones de diseño apropiados
- Manejar recursos y excepciones correctamente
- Validar integridad de datos en múltiples niveles
- Documentar código de forma profesional

---

**Proyecto Educativo** - Trabajo Práctico Integrador de Programación 2
**Integrantes**
- Ataide Benjamin
- Buchek Lautaro
- Castellini Gonzalo
