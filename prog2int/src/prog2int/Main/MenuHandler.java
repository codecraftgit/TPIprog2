package prog2int.Main;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import prog2int.Models.Empresa;
import prog2int.Models.Envio;
import prog2int.Models.EstadoEnvio;
import prog2int.Models.EstadoPedido;
import prog2int.Models.Pedido;
import prog2int.Models.TipoEnvio;
import prog2int.Service.EnvioServiceImpl;
import prog2int.Service.PedidoServiceImpl;

/**
 * Controlador de interacción por consola para operaciones con Pedido/Envio.
 * No contiene reglas de negocio; delega en la capa Service.
 */
public class MenuHandler {

    private final Scanner scanner;
    private final PedidoServiceImpl pedidoService;
    private final EnvioServiceImpl envioService;

    public MenuHandler(Scanner scanner, PedidoServiceImpl pedidoService, EnvioServiceImpl envioService) {
        if (scanner == null) throw new IllegalArgumentException("Scanner no puede ser null");
        if (pedidoService == null) throw new IllegalArgumentException("PedidoService no puede ser null");
        if (envioService == null) throw new IllegalArgumentException("EnvioService no puede ser null");
        this.scanner = scanner;
        this.pedidoService = pedidoService;
        this.envioService = envioService;
    }

    /* ===================== PEDIDOS ===================== */

    public void crearPedido() {
        try {
            System.out.print("Número (máx 20): ");
            String numero = scanner.nextLine().trim();

            LocalDate fecha = readDateOrNull("Fecha (YYYY-MM-DD, Enter para vacío): ");

            System.out.print("Cliente (nombre completo, Enter para vacío): ");
            String cliente = scanner.nextLine().trim();

            System.out.print("Total: ");
            double total = Double.parseDouble(scanner.nextLine());

            EstadoPedido estado = readEstadoPedido();

            Pedido p = new Pedido();
            p.setNumero(numero);
            p.setFecha(fecha);
            p.setClienteNombre(cliente.isEmpty() ? null : cliente);
            p.setTotal(total);
            p.setEstado(estado);

            pedidoService.insertar(p);
            System.out.println("Pedido creado. ID: " + p.getId());
        } catch (Exception e) {
            System.err.println("Error al crear pedido: " + e.getMessage());
        }
    }

    public void listarPedidos() {
        try {
            List<Pedido> pedidos = pedidoService.getAll();
            if (pedidos.isEmpty()) {
                System.out.println("No hay pedidos.");
                return;
            }
            for (Pedido p : pedidos) {
                System.out.println("ID=" + p.getId() +
                        " | nro=" + p.getNumero() +
                        " | estado=" + p.getEstado() +
                        " | total=" + p.getTotal());
            }
        } catch (Exception e) {
            System.err.println("Error al listar pedidos: " + e.getMessage());
        }
    }

    public void actualizarPedido() {
        try {
            System.out.print("ID del pedido a actualizar: ");
            int id = Integer.parseInt(scanner.nextLine());
            Pedido p = pedidoService.getById(id);
            if (p == null) {
                System.out.println("Pedido no encontrado.");
                return;
            }
            // Campos
            System.out.print("Nuevo número (" + p.getNumero() + ", Enter para mantener): ");
            String numero = scanner.nextLine().trim();
            if (!numero.isEmpty()) p.setNumero(numero);

            LocalDate fecha = readDateOrKeep("Nueva fecha (" + p.getFecha() + "): ", p.getFecha());
            p.setFecha(fecha);

            System.out.print("Nuevo cliente (" + p.getClienteNombre() + ", Enter para mantener): ");
            String cliente = scanner.nextLine().trim();
            if (!cliente.isEmpty()) p.setClienteNombre(cliente);

            System.out.print("Nuevo total (" + p.getTotal() + ", Enter para mantener): ");
            String totalTxt = scanner.nextLine().trim();
            if (!totalTxt.isEmpty()) p.setTotal(Double.parseDouble(totalTxt));

            System.out.print("Nuevo estado (" + p.getEstado() + ", Enter para mantener): ");
            String estadoTxt = scanner.nextLine().trim();
            if (!estadoTxt.isEmpty()) p.setEstado(EstadoPedido.valueOf(estadoTxt.toUpperCase()));

            pedidoService.actualizar(p);
            System.out.println("Pedido actualizado.");
        } catch (Exception e) {
            System.err.println("Error al actualizar pedido: " + e.getMessage());
        }
    }

    public void eliminarPedido() {
        try {
            System.out.print("ID del pedido a eliminar (soft delete): ");
            int id = Integer.parseInt(scanner.nextLine());
            pedidoService.eliminar(id);
            System.out.println("Pedido eliminado.");
        } catch (Exception e) {
            System.err.println("Error al eliminar pedido: " + e.getMessage());
        }
    }

    public void buscarPedidoPorNumero() {
        try {
            System.out.print("Número de pedido: ");
            String numero = scanner.nextLine().trim();
            Pedido p = pedidoService.getByNumero(numero);
            if (p == null) {
                System.out.println("No existe pedido con número " + numero);
                return;
            }
            System.out.println("ID=" + p.getId() + " | estado=" + p.getEstado() +
                    " | total=" + p.getTotal());
        } catch (Exception e) {
            System.err.println("Error en búsqueda: " + e.getMessage());
        }
    }

    /* ===================== ENVIOS ===================== */

    public void crearEnvioIndependiente() {
        try {
            Envio e = crearEnvioInteractivo();
            envioService.insertar(e);
            System.out.println("Envío creado. ID: " + e.getId());
        } catch (Exception ex) {
            System.err.println("Error al crear envío: " + ex.getMessage());
        }
    }

    public void listarEnvios() {
        try {
            List<Envio> envios = envioService.getAll();
            if (envios.isEmpty()) {
                System.out.println("No hay envíos.");
                return;
            }
            for (Envio e : envios) {
                System.out.println("ID=" + e.getId() +
                        " | tracking=" + e.getTracking() +
                        " | empresa=" + e.getEmpresa() +
                        " | tipo=" + e.getTipo() +
                        " | estado=" + e.getEstado() +
                        " | costo=" + e.getCosto());
            }
        } catch (Exception ex) {
            System.err.println("Error al listar envíos: " + ex.getMessage());
        }
    }

    public void actualizarEnvioPorId() {
        try {
            System.out.print("ID del envío a actualizar: ");
            int id = Integer.parseInt(scanner.nextLine());
            Envio e = envioService.getById(id);
            if (e == null) {
                System.out.println("Envío no encontrado.");
                return;
            }

            System.out.print("Nuevo tracking (" + e.getTracking() + ", Enter para mantener): ");
            String tracking = scanner.nextLine().trim();
            if (!tracking.isEmpty()) e.setTracking(tracking);

            Empresa empresa = readEmpresaOrKeep("Nueva empresa (" + e.getEmpresa() + "): ", e.getEmpresa());
            e.setEmpresa(empresa);

            TipoEnvio tipo = readTipoEnvioOrKeep("Nuevo tipo (" + e.getTipo() + "): ", e.getTipo());
            e.setTipo(tipo);

            System.out.print("Nuevo costo (" + e.getCosto() + ", Enter para mantener): ");
            String costoTxt = scanner.nextLine().trim();
            if (!costoTxt.isEmpty()) e.setCosto(Double.parseDouble(costoTxt));

            e.setFechaDespacho(readDateOrKeep("Nueva fecha despacho (" + e.getFechaDespacho() + "): ", e.getFechaDespacho()));
            e.setFechaEstimada(readDateOrKeep("Nueva fecha estimada (" + e.getFechaEstimada() + "): ", e.getFechaEstimada()));

            EstadoEnvio est = readEstadoEnvioOrKeep("Nuevo estado (" + e.getEstado() + "): ", e.getEstado());
            e.setEstado(est);

            envioService.actualizar(e);
            System.out.println("Envío actualizado.");
        } catch (Exception ex) {
            System.err.println("Error al actualizar envío: " + ex.getMessage());
        }
    }

    public void eliminarEnvioPorId() {
        try {
            System.out.print("ID del envío a eliminar (soft delete): ");
            int id = Integer.parseInt(scanner.nextLine());
            envioService.eliminar(id);
            System.out.println("Envío eliminado.");
        } catch (Exception ex) {
            System.err.println("Error al eliminar envío: " + ex.getMessage());
        }
    }

    /* ===================== Helpers de entrada ===================== */

    private Envio crearEnvioInteractivo() {
        Envio e = new Envio();
        System.out.print("Tracking (opcional, máx 40, Enter para vacío): ");
        String tracking = scanner.nextLine().trim();
        e.setTracking(tracking.isEmpty() ? null : tracking);

        e.setEmpresa(readEmpresa());
        e.setTipo(readTipoEnvio());

        System.out.print("Costo: ");
        e.setCosto(Double.parseDouble(scanner.nextLine()));

        e.setFechaDespacho(readDateOrNull("Fecha despacho (YYYY-MM-DD, Enter para vacío): "));
        e.setFechaEstimada(readDateOrNull("Fecha estimada (YYYY-MM-DD, Enter para vacío): "));

        e.setEstado(readEstadoEnvio());
        return e;
    }

    private LocalDate readDateOrNull(String prompt) {
        System.out.print(prompt);
        String txt = scanner.nextLine().trim();
        if (txt.isEmpty()) return null;
        try { return LocalDate.parse(txt); }
        catch (DateTimeParseException e) { throw new IllegalArgumentException("Fecha inválida: " + txt); }
    }

    private LocalDate readDateOrKeep(String prompt, LocalDate current) {
        System.out.print(prompt);
        String txt = scanner.nextLine().trim();
        if (txt.isEmpty()) return current;
        try { return LocalDate.parse(txt); }
        catch (DateTimeParseException e) { throw new IllegalArgumentException("Fecha inválida: " + txt); }
    }

    private EstadoPedido readEstadoPedido() {
        System.out.print("Estado de pedido (NUEVO/FACTURADO/ENVIADO): ");
        return EstadoPedido.valueOf(scanner.nextLine().trim().toUpperCase());
    }

    private Empresa readEmpresa() {
        System.out.print("Empresa (ANDREANI/OCA/CORREO_ARG): ");
        return Empresa.valueOf(scanner.nextLine().trim().toUpperCase());
    }

    private Empresa readEmpresaOrKeep(String prompt, Empresa current) {
        System.out.print(prompt);
        String v = scanner.nextLine().trim();
        return v.isEmpty() ? current : Empresa.valueOf(v.toUpperCase());
    }

    private TipoEnvio readTipoEnvio() {
        System.out.print("Tipo envío (ESTANDAR/EXPRESS): ");
        return TipoEnvio.valueOf(scanner.nextLine().trim().toUpperCase());
    }

    private TipoEnvio readTipoEnvioOrKeep(String prompt, TipoEnvio current) {
        System.out.print(prompt);
        String v = scanner.nextLine().trim();
        return v.isEmpty() ? current : TipoEnvio.valueOf(v.toUpperCase());
    }

    private EstadoEnvio readEstadoEnvio() {
        System.out.print("Estado envío (EN_PREPARACION/EN_TRANSITO/ENTREGADO): ");
        return EstadoEnvio.valueOf(scanner.nextLine().trim().toUpperCase());
    }

    private EstadoEnvio readEstadoEnvioOrKeep(String prompt, EstadoEnvio current) {
        System.out.print(prompt);
        String v = scanner.nextLine().trim();
        return v.isEmpty() ? current : EstadoEnvio.valueOf(v.toUpperCase());
    }
}
